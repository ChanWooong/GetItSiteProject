package com.getit.domain.assignment.service;

import com.getit.domain.assignment.dto.FileDownloadDto;
import com.getit.domain.assignment.dto.AdminAssignmentMapper;
import com.getit.domain.assignment.dto.AdminAssignmentDetailResponse;
import com.getit.domain.assignment.dto.AdminAssignmentListResponse;
import com.getit.domain.assignment.entity.Assignment;
import com.getit.domain.assignment.entity.AssignmentFile;
import com.getit.domain.assignment.repository.AssignmentFileRepository;
import com.getit.domain.assignment.repository.AssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminAssignmentServiceImpl implements AdminAssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final AssignmentFileRepository assignmentFileRepository;

    @Value("${file.upload.dir}")
    private String storagePath;

    // 부원들이 제출한 전체 과제 조회 
    @Override
    public Page<AdminAssignmentListResponse> getAllAssignments(Pageable pageable) {

        // Assignment 페이징 조회
        Page<Assignment> assignmentPage = assignmentRepository.findAllWithTaskAndLecture(pageable);

        List<Assignment> assignments = assignmentPage.getContent();

        if (assignments.isEmpty()) {
            return Page.empty(pageable);
        }

        // assignmentId 목록 추출
        List<Long> assignmentIds = assignments.stream()
                .map(Assignment::getId)
                .toList();

        // AssignmentFile 조회
        List<AssignmentFile> files =
                assignmentFileRepository.findByAssignmentIdIn(assignmentIds);

        // assignmentId 기준 Map 생성
        Map<Long, List<AssignmentFile>> fileMap =
                files.stream()
                        .collect(Collectors.groupingBy(
                                file -> file.getAssignment().getId()
                        ));

        // Entity → DTO 변환
        List<AdminAssignmentListResponse> dtoList =
                AdminAssignmentMapper.toResponseList(assignments, fileMap);

        return new PageImpl<>(dtoList, pageable, assignmentPage.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public AdminAssignmentDetailResponse getAssignmentDetail(Long lectureId, Long memberId) {

        Assignment assignment = assignmentRepository.findByTaskLectureIdAndMemberId(lectureId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 과제를 찾을 수 없습니다."));

        return AdminAssignmentMapper.toDetailResponse(assignment);
    }

    @Override
    @Transactional(readOnly = true)
    public FileDownloadDto downloadFile(Long fileId) {

        AssignmentFile assignmentFile = assignmentFileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("해당 파일을 찾을 수 없습니다."));

        try {
            Path filePath = getValidatedFilePath(assignmentFile.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new IllegalStateException("파일을 읽을 수 없거나 존재하지 않습니다.");
            }

            return new FileDownloadDto(resource, assignmentFile.getFileName());

        } catch (MalformedURLException e) {
            throw new IllegalStateException("파일 경로가 잘못되었습니다.");
        }
    }

    private Path getValidatedFilePath(String filePath) {
        Path root = Path.of(storagePath).toAbsolutePath().normalize();
        Path path = Path.of(filePath).toAbsolutePath().normalize();

        if (!path.startsWith(root)) {
            throw new SecurityException("허용되지 않은 파일 경로 접근 시도: " + filePath);
        }
        return path;
    }
}