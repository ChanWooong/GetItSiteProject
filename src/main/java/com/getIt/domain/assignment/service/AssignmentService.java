package com.getit.domain.assignment.service;

import com.getit.domain.assignment.dto.*;
import com.getit.domain.assignment.entity.Assignment;
import com.getit.domain.assignment.entity.AssignmentFile;
import com.getit.domain.assignment.entity.Task;
import com.getit.domain.assignment.repository.AssignmentFileRepository;
import com.getit.domain.assignment.repository.AssignmentRepository;
import com.getit.domain.assignment.repository.TaskRepository;
import com.getit.domain.lecture.entity.Lecture;
import com.getit.domain.lecture.repository.LectureRepository;
import com.getit.domain.member.entity.Member;
import com.getit.domain.member.repository.MemberRepository;
import com.getit.global.exception.ErrorCode;
import com.getit.global.exception.GlobalExceptionManager.BusinessException;
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
    private final LectureRepository lectureRepository;
    private final FileStorageService fileStorageService;

    @Transactional
    public AssignmentResultDto createAssignment(Long memberId, List<MultipartFile> files, AssignmentSubmitRequest dto) {
        // 1. 파일 기본 검증
        validateFiles(files);

        // 2. 관련 엔티티 조회 (BusinessException 적용)
        Member member = findMember(memberId);
        Lecture lecture = lectureRepository.findByWeekAndType(dto.getWeek(), dto.getType())
                .orElseThrow(() -> new BusinessException(ErrorCode.LECTURE_NOT_FOUND));
        Task task = taskRepository.findByLecture(lecture)
                .orElseThrow(() -> new BusinessException(ErrorCode.TASK_NOT_FOUND));

        String dirName = UUID.randomUUID().toString();

        Assignment assignment = Assignment.builder()
                .task(task)
                .member(member)
                .status(task.determineSubmitStatus(LocalDateTime.now()))
                .dirName(dirName)
                .comment(dto.getComment())
                .githubUrl(StringUtils.hasText(dto.getGithubUrl()) ? dto.getGithubUrl() : null)
                .build();

        // 3. 중복 제출 체크
        try {
            assignmentRepository.saveAndFlush(assignment);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(ErrorCode.ALREADY_SUBMITTED_ASSIGNMENT);
        }

        // 4. 물리 파일 저장 및 예외 처리
        fileStorageService.createAssignmentDir(dirName);
        try {
            return processAndSaveFiles(files, assignment, dirName);
        } catch (Exception e) {
            fileStorageService.deleteDir(dirName);
            // 래핑된 BusinessException이라면 그대로 던지고, 아니라면 새로 생성
            if (e instanceof BusinessException) throw e;
            throw new BusinessException(ErrorCode.FILE_UPLOAD_ERROR, "파일 저장 중 오류가 발생했습니다.");
        }
    }

    @Transactional
    public AssignmentUpdateResultDto updateAssignment(Long memberId, List<MultipartFile> newFiles, Long assignmentId, AssignmentUpdateRequest dto) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ASSIGNMENT_NOT_FOUND));

        if (!assignment.getMember().getId().equals(memberId)) {
            throw new BusinessException(ErrorCode.NOT_ASSIGNMENT_OWNER);
        }

        // 파일 개수 검증 (도우미 메서드로 분리)
        validateUpdateFileCount(assignment, newFiles, dto);

        AssignmentResultDto result = null;
        List<String> deletedFilePaths = new ArrayList<>();
        boolean hasRealNewFiles = isNotEmptyFiles(newFiles);

        try {
            if (hasRealNewFiles) {
                result = processAndSaveFiles(newFiles, assignment, assignment.getDirName());
            }

            if (dto != null && dto.getDeletedFiles() != null && !dto.getDeletedFiles().isEmpty()) {
                deletedFilePaths = extractValidDeletePaths(assignment, dto.getDeletedFiles());
            }

            // 엔티티 업데이트
            assignment.updateStatus(assignment.getTask().determineSubmitStatus(LocalDateTime.now()));
            assignment.updateComment(dto != null ? dto.getComment() : null);
            if (dto != null) assignment.updateGithubUrl(dto.getGithubUrl());

            // 커밋 후 물리 파일 삭제 예약
            registerFileDeletionSync(deletedFilePaths);

            return AssignmentUpdateResultDto.builder()
                    .assignmentId(assignment.getId())
                    .status(assignment.getStatus())
                    .updatedAt(LocalDateTime.now().toString())
                    .successFiles(result != null ? result.getSuccessFiles() : new ArrayList<>())
                    .build();

        } catch (Exception e) {
            rollbackUploadedFiles(assignment);
            if (e instanceof BusinessException) throw e;
            throw new BusinessException(ErrorCode.FILE_UPLOAD_ERROR, "수정 중 오류가 발생했습니다.");
        }
    }

    // ── private 헬퍼 메서드 (리팩토링 핵심) ─────────────────────────

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private void validateFiles(List<MultipartFile> files) {
        if (!isNotEmptyFiles(files)) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "최소 1개 이상의 파일을 업로드해야 합니다.");
        }
        if (files.size() > maxFileCount) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "파일은 최대 " + maxFileCount + "개까지 첨부 가능합니다.");
        }
    }

    private boolean isNotEmptyFiles(List<MultipartFile> files) {
        return files != null && !files.isEmpty() && !(files.size() == 1 && files.get(0).isEmpty());
    }

    private List<String> extractValidDeletePaths(Assignment assignment, List<Long> requestedDeletedFileIds) {
        List<AssignmentFile> currentFiles = assignment.getAssignmentFiles();
        List<Long> currentFileIds = currentFiles.stream().map(AssignmentFile::getId).toList();

        if (!currentFileIds.containsAll(requestedDeletedFileIds)) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "삭제 대상 파일 ID가 유효하지 않습니다.");
        }

        List<String> paths = currentFiles.stream()
                .filter(f -> requestedDeletedFileIds.contains(f.getId()))
                .map(AssignmentFile::getFilePath)
                .toList();

        // DB 관계 삭제
        assignment.getAssignmentFiles().removeIf(f -> requestedDeletedFileIds.contains(f.getId()));
        return paths;
    }

    private void registerFileDeletionSync(List<String> filePaths) {
        if (filePaths.isEmpty()) return;
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                filePaths.forEach(path -> {
                    try { fileStorageService.deleteFile(path); }
                    catch (Exception e) { log.error("물리 파일 삭제 실패: {}", path, e); }
                });
            }
        });
    }

    private void rollbackUploadedFiles(Assignment assignment) {
        assignment.getAssignmentFiles().stream()
                .filter(f -> f.getId() == null) // 아직 저장되지 않은 파일만
                .forEach(f -> {
                    try { fileStorageService.deleteFile(f.getFilePath()); }
                    catch (Exception ex) { log.error("보상 트랜잭션 실패: {}", f.getFilePath(), ex); }
                });
    }
    private void validateUpdateFileCount(Assignment assignment, List<MultipartFile> newFiles, AssignmentUpdateRequest dto) {
        int existingFileCount = assignment.getAssignmentFiles().size();
        List<Long> requestedDeletedFileIds = (dto != null && dto.getDeletedFiles() != null)
                ? dto.getDeletedFiles().stream().distinct().toList()
                : List.of();

        // 유효성 검사 (기존 코드 로직 유지)
        int deletedFileCount = (int) assignment.getAssignmentFiles().stream()
                .filter(f -> requestedDeletedFileIds.contains(f.getId())).count();

        if (deletedFileCount != requestedDeletedFileIds.size()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "삭제 대상 파일 ID가 유효하지 않습니다.");
        }

        int actualNewFileCount = isNotEmptyFiles(newFiles) ? newFiles.size() : 0;
        int finalFileCount = existingFileCount - deletedFileCount + actualNewFileCount;

        if (finalFileCount > maxFileCount) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "파일은 최대 " + maxFileCount + "개까지만 유지할 수 있습니다.");
        }
        if (finalFileCount < 1) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "최소 1개 이상의 파일이 유지되어야 합니다.");
        }
    }
    private AssignmentResultDto processAndSaveFiles(List<MultipartFile> files, Assignment assignment, String dirName) {
        List<String> successFileNames = new ArrayList<>();
        List<String> failedFileNames = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            if (!StringUtils.hasText(fileName) || file.isEmpty()) continue;

            String extension = StringUtils.getFilenameExtension(fileName);
            if (extension == null || allowedExtensions.stream().noneMatch(allowed -> allowed.equalsIgnoreCase(extension))) {
                failedFileNames.add(fileName);
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
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "유효한 파일이 없습니다.");
        }

        return AssignmentResultDto.builder()
                .successFiles(successFileNames)
                .failedFiles(failedFileNames)
                .build();
    }
    @Transactional(readOnly = true)
    public List<AssignmentReadResultDto> getAssignments(Long memberId) {
        // 사용자 존재 여부 확인
        findMember(memberId);

        // 멤버 ID로 과제, 과제에 속한 Task, 파일들을 한꺼번에 조회
        List<Assignment> assignments = assignmentRepository.findAllByMemberIdWithTaskAndFiles(memberId);

        return assignments.stream()
                .map(assignment -> {
                    Task task = assignment.getTask();
                    Lecture lecture = task.getLecture();

                    return AssignmentReadResultDto.builder()
                            .assignmentId(assignment.getId())
                            .week(lecture.getWeek())
                            .type(lecture.getType())
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
                            .githubUrl(assignment.getGithubUrl())
                            .build();
                })
                .toList();
    }
}