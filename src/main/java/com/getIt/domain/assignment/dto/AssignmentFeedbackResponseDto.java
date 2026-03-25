package com.getit.domain.assignment.dto;

import com.getit.domain.assignment.entity.AssignmentFeedback;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentFeedbackResponseDto {

    private Long feedbackId;
    private Long assignmentId;
    private String content;
    private String createdAt;
    private String updatedAt;

    public static AssignmentFeedbackResponseDto from(AssignmentFeedback feedback) {
        return AssignmentFeedbackResponseDto.builder()
                .feedbackId(feedback.getId())
                .assignmentId(feedback.getAssignment().getId())
                .content(feedback.getContent())
                .createdAt(feedback.getCreatedAt() != null ? feedback.getCreatedAt().toString() : null)
                .updatedAt(feedback.getUpdatedAt() != null ? feedback.getUpdatedAt().toString() : null)
                .build();
    }
}

