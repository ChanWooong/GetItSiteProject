package com.getit.domain.assignment.service;

import com.getit.domain.assignment.dto.*;
import com.getit.domain.assignment.entity.Assignment;
import com.getit.domain.assignment.entity.AssignmentFile;
import com.getit.domain.assignment.entity.Task;
import com.getit.domain.assignment.repository.AssignmentFileRepository;
import com.getit.domain.assignment.repository.AssignmentRepository;
import com.getit.domain.assignment.repository.TaskRepository;
import com.getit.domain.member.entity.Member;
import com.getit.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssignmentService {

    @Value("${file.upload.allowed-extensions}")
    private List<String> allowedExtensions;

    @Value("${file.upload.max-count}")
    private long maxFileCount;

    private final AssignmentRepository assignmentRepository;
    private final AssignmentFileRepository assignmentFileRepository;
    private final TaskRepository taskRepository;
    private final MemberRepository memberRepository;
    private final FileStorageService fileStorageService;

    @Transactional
    public AssignmentResultDto createAssignment(Long memberId, List<MultipartFile> files, AssignmentSubmitRequest dto) {
        if (files == null || files.isEmpty() || (files.size() == 1 && files.get(0).isEmpty())) {
            throw new IllegalArgumentException("첨부된 파일이 없습니다. 최소 1개 이상의 파일을 업로드해야 합니다.");
        }
        if (files.size() > maxFileCount) {
            throw new IllegalArgumentException("파일은 최대 " + maxFileCount + "개 까지만 첨부 가능합니다.");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Task task = taskRepository.findByTypeAndWeek(dto.getType(), dto.getWeek())
                .orElseThrow(() -> new IllegalArgumentException("해당 task를 찾을 수 없습니다."));

        String dirName = UUID.randomUUID().toString();

        LocalDateTime submittedAt = LocalDateTime.now();
        Assignment assignment = Assignment.builder()
                .task(task)
                .member(member)
                .status(task.determineSubmitStatus(submittedAt))
                .dirName(dirName)
                .comment(dto.getComment())
                .build();

        try {
            assignmentRepository.saveAndFlush(assignment);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("이미 제출된 과제입니다.");
        }

        fileStorageService.createAssignmentDir(dirName);

        try {
            return processAndSaveFiles(files, assignment, dirName);
        } catch (Exception e) {
            fileStorageService.deleteDir(dirName); // 롤백
            throw new IllegalStateException("저장 중 오류 발생으로 전체 롤백되었습니다.", e); // 트랜잭션 동작
        }
    }

    @Transactional
    public AssignmentUpdateResultDto updateAssignment(
            Long memberId, List<MultipartFile> newFiles, Long assignmentId, AssignmentUpdateRequest dto
    ) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 과제를 찾을 수 없습니다."));

        if (!assignment.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("본인이 제출한 과제만 수정할 수 있습니다.");
        }

        // ================================================================================================ //
        int existingFileCount = assignment.getAssignmentFiles().size();
        List<Long> requestedDeletedFileIds = (dto != null && dto.getDeletedFiles() != null)
                ? dto.getDeletedFiles().stream().distinct().toList()
                : List.of();
        List<Long> currentFileIds = assignment.getAssignmentFiles().stream().map(AssignmentFile::getId).toList();

        List<Long> validDeleteFileIds = requestedDeletedFileIds.stream()
                .filter(currentFileIds::contains)
                .toList();

        if (validDeleteFileIds.size() != requestedDeletedFileIds.size()) {
            throw new IllegalArgumentException("삭제 대상 파일 ID가 유효하지 않습니다.");
        }
        int deletedFileCount = validDeleteFileIds.size();

        int actualNewFileCount = 0;
        boolean hasRealNewFiles =
                (newFiles != null && !newFiles.isEmpty() && !(newFiles.size() == 1 && newFiles.get(0).isEmpty()));

        if (hasRealNewFiles) {
            actualNewFileCount = newFiles.size();
        }

        int finalFileCount = existingFileCount - deletedFileCount + actualNewFileCount;

        if (finalFileCount > maxFileCount) {
            throw new IllegalArgumentException("파일은 최대 " + maxFileCount + "개 까지만 유지할 수 있습니다.");
        }

        if (finalFileCount < 1) {
            throw new IllegalArgumentException("최소 1개 이상의 파일이 유지되어야 합니다.");
        }
        // ================================================================================================ //

        AssignmentResultDto result = null;
        List<String> deletedFilePaths = new ArrayList<>();

        try {
            if (hasRealNewFiles) {
                String dirName = assignment.getDirName();
                result = processAndSaveFiles(newFiles, assignment, dirName);
            }

            if (!validDeleteFileIds.isEmpty()) {
                deletedFilePaths = assignment.getAssignmentFiles().stream()
                        .filter(file -> validDeleteFileIds.contains(file.getId()))
                        .map(AssignmentFile::getFilePath)
                        .toList();

                // DB 우선 삭제
                assignment.getAssignmentFiles().removeIf(file -> validDeleteFileIds.contains(file.getId()));
            }

            LocalDateTime updatedAt = LocalDateTime.now();
            assignment.updateStatus(assignment.getTask().determineSubmitStatus(updatedAt));
            assignment.updateComment(dto != null && StringUtils.hasText(dto.getComment()) ? dto.getComment() : null);

            if (!deletedFilePaths.isEmpty()) {
                List<String> finalDeletedFilePaths = deletedFilePaths;
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        for (String filePath : finalDeletedFilePaths) {
                            try {
                                fileStorageService.deleteFile(filePath);
                            } catch (Exception e) {
                                log.error("DB commit 완료 후 물리 파일 삭제 실패: {}", filePath, e);
                            }
                        }
                    }

                });
            }

            return AssignmentUpdateResultDto.builder()
                    .assignmentId(assignment.getId())
                    .status(assignment.getStatus())
                    .updatedAt(assignment.getUpdatedAt() != null ? assignment.getUpdatedAt().toString() : null)
                    .successFiles(result != null ? result.getSuccessFiles() : new ArrayList<>())
                    .failedFiles(result != null ? result.getFailedFiles() : new ArrayList<>())
                    .build();
        } catch (Exception e) {
            if (hasRealNewFiles) {
                for (AssignmentFile file : assignment.getAssignmentFiles()) {
                    if (file.getId() == null) {
                        try {
                            fileStorageService.deleteFile(file.getFilePath());
                        } catch (Exception ex) {
                            log.error("보상 트랜잭션 실패{}", file.getFilePath(), ex);
                        }
                    }
                }
            }
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<AssignmentReadResultDto> getAssignments(Long memberId) {
        List<Assignment> assignments = assignmentRepository.findAllByMemberIdWithTaskAndFiles(memberId);
        return assignments.stream()
                .map(assignment -> {
                    Task task = assignment.getTask();
                    return AssignmentReadResultDto.builder()
                            .assignmentId(assignment.getId())
                            .week(task.getWeek())
                            .type(task.getType())
                            .status(assignment.getStatus())
                            .files(
                                    assignment.getAssignmentFiles().stream()
                                            .map(file -> AssignmentReadResultDto.AssignmentFileInfo.builder()
                                                    .fileId(file.getId())
                                                    .fileName(file.getFileName())
                                                    .build()
                                            ).toList()
                            )
                            .createdAt(assignment.getSubmittedAt() != null ? assignment.getSubmittedAt().toString() : null)
                            .updatedAt(assignment.getUpdatedAt() != null ? assignment.getUpdatedAt().toString() : null)
                            .deadline(task.getDeadline() != null ? task.getDeadline().toString() : null)
                            .build();
                })
                .toList();
    }

    private AssignmentResultDto processAndSaveFiles(
            List<MultipartFile> files, Assignment assignment, String dirName
    ) {
        List<String> successFileNames = new ArrayList<>();
        List<String> failedFileNames = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();

            if (!StringUtils.hasText(fileName)) {
                log.warn("올바르지 않은 이름의 파일 스킵됨({})", fileName);
                failedFileNames.add(fileName != null ? fileName : "Unknown_File");
                continue;
            }
            if (file.isEmpty()) {
                log.warn("비어있는 파일 스킵됨({})", fileName);
                failedFileNames.add(fileName);
                continue;
            }

            String extension = StringUtils.getFilenameExtension(fileName);
            if (extension == null ||
                    allowedExtensions.stream().noneMatch(allowed -> allowed.equalsIgnoreCase(extension))) {
                failedFileNames.add(fileName);
                log.warn("허용되지 않는 확장자의 파일 스킵됨({})", fileName);
                continue;
            }

            String filePath = fileStorageService.storeFile(file, dirName);

            AssignmentFile assignmentFile = AssignmentFile.builder()
                    .assignment(assignment)
                    .fileName(fileName)
                    .filePath(filePath)
                    .build();
            assignment.addAssignmentFile(assignmentFile);

            successFileNames.add(fileName);
        }

        if (successFileNames.isEmpty()) {
            throw new IllegalArgumentException("유효한 파일이 없습니다.");
        }

        return AssignmentResultDto.builder()
                .successFiles(successFileNames)
                .failedFiles(failedFileNames)
                .build();
    }
}

