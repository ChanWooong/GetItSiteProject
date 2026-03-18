package com.getit.domain.admin.assignment.dto.response;

import com.getit.domain.assignment.Status;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class AdminAssignmentDetailResponse {
    // 과제 메타 데이터
    private Long assignmentId;
    private Status status;
    private String comment;
    private LocalDateTime submittedAt;
    private LocalDateTime updatedAt;

    // 과제 제출 파일 목록
    private List<AssignmentFileInfoDto> files;

    // 부가 내용
    private Long memberId;
    private String memberName;
    private String taskTitle;
}
