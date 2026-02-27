package com.getit.domain.assignment.exception;

import com.getit.domain.assignment.controller.AssignmentController;
import com.getit.domain.assignment.dto.AssignmentTemporaryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(assignableTypes = AssignmentController.class)
public class AssignmentExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<AssignmentTemporaryResponse<Void>> handleIllegalArgumentException(
            IllegalArgumentException e
    ) {
        log.warn("과제 클라이언트 요청 오류: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(AssignmentTemporaryResponse.fail(e.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<AssignmentTemporaryResponse<Void>> handleIllegalStateException(IllegalStateException e) {
        log.warn("과제 비즈니스 상태 오류: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(AssignmentTemporaryResponse.fail(e.getMessage()));
    }
}
