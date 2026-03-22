package com.getit.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionManager {
    // 2. 비즈니스 예외 클래스 (BusinessException)
    @Getter
    public static class BusinessException extends RuntimeException {
        private final ErrorCode errorCode;
        private final String customMessage;

        public BusinessException(ErrorCode errorCode) {
            super(errorCode.getMessage());
            this.errorCode = errorCode;
            this.customMessage = errorCode.getMessage();
        }

        public BusinessException(ErrorCode errorCode, String customMessage){
            super(customMessage);
            this.errorCode = errorCode;
            this.customMessage = customMessage;
        }
    }

    // 3. 에러 응답 포맷 (ErrorResponse)
    @Getter
    public static class ErrorResponse {
        private final LocalDateTime timestamp = LocalDateTime.now();
        private final int status;
        private final String code;
        private final String message;

        // Enum에 있는 기본 메시지 사용
        public ErrorResponse(ErrorCode errorCode) {
            this.status = errorCode.getStatus().value();
            this.code = errorCode.getCode();
            this.message = errorCode.getMessage();
        }

        // @Valid 등에서 발생하는 동적 메시지로 덮어쓰기
        public ErrorResponse(ErrorCode errorCode, String customMessage) {
            this.status = errorCode.getStatus().value();
            this.code = errorCode.getCode();
            this.message = customMessage;
        }
    }

    // 4. 전역 예외 처리 핸들러 (Exception Handlers)
    // (1) 우리가 직접 던진 비즈니스 예외 처리 (throw new BusinessException(...))
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.warn("BusinessException: {}", e.getCustomMessage());
        return new ResponseEntity<>(
                new ErrorResponse(e.getErrorCode(), e.getCustomMessage()),
                e.getErrorCode().getStatus()
        );
    }

    // (2) @Valid 유효성 검사 실패 (@NotNull, @Positive 등)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // DTO에 설정한 에러 메시지를 그대로 가져와서 클라이언트에게 전달
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.warn("MethodArgumentNotValidException: {}", errorMessage);
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.INVALID_INPUT_VALUE, errorMessage), HttpStatus.BAD_REQUEST);
    }

    // (3) 파일 업로드 용량 초과 에러
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    protected ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error("MaxUploadSizeExceededException: {}", e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.MAX_UPLOAD_SIZE_EXCEEDED), HttpStatus.PAYLOAD_TOO_LARGE);
    }

    // (4) 지원하지 않는 HTTP 메서드 (GET인데 POST로 요청 등)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.warn("HttpRequestMethodNotSupportedException: {}", e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.METHOD_NOT_ALLOWED), HttpStatus.METHOD_NOT_ALLOWED);
    }

    // (5) JSON 파싱 에러 (포스트맨에서 JSON 포맷 틀렸을 때)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("HttpMessageNotReadableException: {}", e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.INVALID_INPUT_VALUE, "요청 데이터 형식이 잘못되었습니다."), HttpStatus.BAD_REQUEST);
    }

    // (6) 파라미터 타입 불일치 (숫자 자리에 문자 등)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.warn("MethodArgumentTypeMismatchException: {}", e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.INVALID_INPUT_VALUE, "파라미터 타입이 일치하지 않습니다."), HttpStatus.BAD_REQUEST);
    }

    // (7) 그 외 모든 복구 불가능한 시스템 에러
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Critical System Error: ", e);
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}