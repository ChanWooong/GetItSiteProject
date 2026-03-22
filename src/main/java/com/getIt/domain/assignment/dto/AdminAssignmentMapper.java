package com.getit.domain.assignment.dto;

import com.getit.domain.assignment.entity.Assignment;
import com.getit.domain.assignment.entity.AssignmentFile;
import com.getit.domain.assignment.entity.Task;
import com.getit.domain.lecture.entity.Lecture;

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
        Lecture lecture = task.getLecture();

        return AdminAssignmentListResponse.builder()
                .assignmentId(assignment.getId())
                .memberId(assignment.getMember().getId())
                .status(assignment.getStatus())
                .submittedAt(assignment.getSubmittedAt())
                .updatedAt(assignment.getUpdatedAt())
                .comment(assignment.getComment())
                .githubUrl(assignment.getGithubUrl())

                .taskId(task.getId())
                .week(lecture.getWeek())
                .trackType(lecture.getType())
                .taskTitle(task.getTitle())
                .deadline(task.getDeadline())

                .files(
                        files.stream()
                                .map(AdminAssignmentMapper::toFileInfo)
                                .collect(Collectors.toList())
                )
                .build();
    }

    public static AdminAssignmentDetailResponse toDetailResponse(Assignment assignment) {

        Task task = assignment.getTask();

        return AdminAssignmentDetailResponse.builder()
                .assignmentId(assignment.getId())
                .status(assignment.getStatus())
                .comment(assignment.getComment())
                .githubUrl(assignment.getGithubUrl())
                .submittedAt(assignment.getSubmittedAt())
                .updatedAt(assignment.getUpdatedAt())
                .memberId(assignment.getMember().getId())
                .memberName(assignment.getMember().getMemberInfo().getName())
                .taskTitle(task.getTitle())
                .files(
                        assignment.getAssignmentFiles().stream()
                                .map(AdminAssignmentMapper::toFileInfo)
                                .collect(Collectors.toList())
                )
                .build();
    }

    // AssignmentFile → FileInfo DTO 변환
    private static AssignmentFileInfoDto toFileInfo(AssignmentFile file) {

        return AssignmentFileInfoDto.builder()
                .fileId(file.getId())
                .fileName(file.getFileName())
                // .filePath(file.getFilePath())
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