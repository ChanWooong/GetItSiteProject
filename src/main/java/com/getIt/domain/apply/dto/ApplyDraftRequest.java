package com.getit.domain.apply.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 임시저장 전용 요청 DTO. 미완성 상태 저장을 위해 검증 없음(필드 선택 입력 가능).
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApplyDraftRequest {

    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String answer5;
    private String answer6;
    private String answer7;
    private Boolean agree;
}
