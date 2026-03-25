package com.getit.domain.assignment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AssignmentFeedbackCreateRequestDto {

    @NotBlank(message = "코멘트 내용은 필수입니다.")
    private String content;
}

