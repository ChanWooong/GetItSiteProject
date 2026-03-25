package com.getit.domain.assignment.dto;

import com.getit.domain.assignment.Status;
import com.getit.domain.assignment.TrackType;
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
    private String memberName;
    private Status status;
    private LocalDateTime submittedAt;
    private LocalDateTime updatedAt;
    private String comment;
    private String githubUrl;

    // Task 정보
    private Long taskId;
    private Integer week;
    private TrackType trackType;
    private String taskTitle;
    private LocalDateTime deadline;

    // 파일 목록
    private List<AssignmentFileInfoDto> files;
}