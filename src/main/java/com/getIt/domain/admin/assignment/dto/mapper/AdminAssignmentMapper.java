package com.getit.domain.admin.assignment.dto.mapper;

import com.getit.domain.admin.assignment.dto.response.AdminAssignmentListResponse;
import com.getit.domain.assignment.entity.Assignment;
import com.getit.domain.assignment.entity.AssignmentFile;
import com.getit.domain.assignment.entity.Task;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class AdminAssignmentMapper {

    // Utility class 인스턴스 생성 방지
    private AdminAssignmentMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    // Assignment + File 목록을 DTO로 변환
    public static AdminAssignmentListResponse toResponse(
            Assignment assignment,
            List<AssignmentFile> files
    ) {

        Task task = assignment.getTask();

        return AdminAssignmentListResponse.builder()
                .assignmentId(assignment.getId())
                .memberId(assignment.getMember().getId())
                .status(assignment.getStatus())
                .submittedAt(assignment.getSubmittedAt())
                .updatedAt(assignment.getUpdatedAt())
                .comment(assignment.getComment())

                .taskId(task.getId())
                .week(task.getWeek())
                .taskType(task.getType())
                .taskTitle(task.getTitle())
                .deadline(task.getDeadline())

                .files(
                        files.stream()
                                .map(AdminAssignmentMapper::toFileInfo)
                                .collect(Collectors.toList())
                )
                .build();
    }

    // AssignmentFile → FileInfo DTO 변환
    private static AdminAssignmentListResponse.FileInfo toFileInfo(AssignmentFile file) {

        return AdminAssignmentListResponse.FileInfo.builder()
                .fileId(file.getId())
                .fileName(file.getFileName())
                .filePath(file.getFilePath())
                .build();
    }

    // Assignment 리스트 + 파일 Map을 DTO 리스트로 변환
    public static List<AdminAssignmentListResponse> toResponseList(
            List<Assignment> assignments,
            Map<Long, List<AssignmentFile>> fileMap
    ) {

        return assignments.stream()
                .map(a -> toResponse(
                        a,
                        fileMap.getOrDefault(a.getId(), List.of())
                ))
                .collect(Collectors.toList());
    }
}