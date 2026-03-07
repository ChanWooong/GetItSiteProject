package com.getit.domain.assignment.dto;

import com.getit.domain.assignment.Status;
import com.getit.domain.assignment.TaskType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AssignmentReadResultDto {
    private Long assignmentId;
    private Integer week;
    private TaskType type;
    private Status status;
    private List<AssignmentFileInfo> files;
    private String createdAt;
    private String updatedAt;
    private String deadline;

    @Getter
    @Builder
    public static class AssignmentFileInfo {
        private Long fileId;
        private String fileName;
    }
}
