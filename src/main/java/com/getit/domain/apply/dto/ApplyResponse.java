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
public class ApplyResponse {

    private boolean success;
    private String message;
    private Boolean submitted;

    public static ApplyResponse success(String message) {
        return ApplyResponse.builder()
                .success(true)
                .message(message)
                .build();
    }
    public static ApplyResponse of(boolean submitted) {
        return ApplyResponse.builder()
                .success(true)
                .submitted(submitted)
                .build();
    }
}
