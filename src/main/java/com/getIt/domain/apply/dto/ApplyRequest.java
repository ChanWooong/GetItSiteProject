package com.getit.domain.apply.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApplyRequest {

    @NotBlank(message = "답변1은 필수 입력 항목입니다.")
    private String answer1;

    @NotBlank(message = "답변2는 필수 입력 항목입니다.")
    private String answer2;

    @NotBlank(message = "답변3은 필수 입력 항목입니다.")
    private String answer3;

    @NotBlank(message = "답변4는 필수 입력 항목입니다.")
    private String answer4;

    @NotBlank(message = "답변5는 필수 입력 항목입니다.")
    private String answer5;

    @NotBlank(message = "답변6는 필수 입력 항목입니다.")
    private String answer6;

    private String answer7;

    @NotNull(message = "필참 동의는 필수입니다.")
    @AssertTrue(message = "필참 동의에 체크해주세요.")
    private Boolean agree;
}
