package com.getit.domain.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

// global apiResponse가 생긴다면 교체
@Getter
@AllArgsConstructor
public class AssignmentTemporaryResponse<T> {

    private boolean success;
    private String message;
    private T data;

    public static <T> AssignmentTemporaryResponse<T> success(String message, T data) {
        return new AssignmentTemporaryResponse<>(true, message, data);
    }

    public static <T> AssignmentTemporaryResponse<T> fail(String message) {
        return new AssignmentTemporaryResponse<>(false, message, null);
    }
}
