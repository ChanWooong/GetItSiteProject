package com.getit.domain.assignment.dto;

import com.getit.domain.assignment.Status;
import com.getit.domain.assignment.TrackType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AssignmentReadResultDto {
    private Long assignmentId;
    private Integer week;
    private TrackType type;
    private Status status;
    private List<AssignmentFileInfo> files;
    private String createdAt;
    private String updatedAt;
    private String deadline;
    private String githubUrl;

    @Getter
    @Builder
    public static class AssignmentFileInfo {
        private Long fileId;
        private String fileName;
    }
}
