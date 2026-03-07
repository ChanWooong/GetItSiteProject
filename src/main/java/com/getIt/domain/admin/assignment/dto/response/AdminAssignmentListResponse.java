package com.getit.domain.admin.assignment.dto.response;

import com.getit.domain.admin.assignment.entity.AssignmentStatus;
import com.getit.domain.admin.assignment.entity.TaskType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class AdminAssignmentListResponse {

    // Assignment 정보
    private Long assignmentId;
    private Long memberId;
    private AssignmentStatus status;
    private LocalDateTime submittedAt;
    private LocalDateTime updatedAt;
    private String comment;

    // Task 정보
    private Long taskId;
    private Integer week;
    private TaskType taskType;
    private String taskTitle;
    private LocalDateTime deadline;

    // 파일 목록
    private List<FileInfo> files;

    @Getter
    @Builder
    public static class FileInfo {

        private Long fileId;
        private String fileName;
        private String filePath;

    }
}