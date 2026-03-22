package com.getit.domain.qna.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QnaRequest {
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
}