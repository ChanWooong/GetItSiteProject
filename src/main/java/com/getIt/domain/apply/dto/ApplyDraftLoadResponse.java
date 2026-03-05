package com.getit.domain.apply.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ApplyDraftLoadResponse {

    private boolean success;
    private String message;
    private ApplyDraftDataDto data;

    public static ApplyDraftLoadResponse of(String message, ApplyDraftDataDto data) {
        return ApplyDraftLoadResponse.builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    /** 드래프트 미존재 시 일관된 응답 스키마 (success=false, data=null) */
    public static ApplyDraftLoadResponse notFound(String message) {
        return ApplyDraftLoadResponse.builder()
                .success(false)
                .message(message)
                .data(null)
                .build();
    }
}
