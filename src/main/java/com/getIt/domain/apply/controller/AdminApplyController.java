package com.getit.domain.apply.controller;

import com.getit.domain.apply.dto.AdminApplyDetailResponse;
import com.getit.domain.apply.dto.AdminApplyListResponse;
import com.getit.domain.apply.service.AdminApplyService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

// 관리자 전용 지원서 조회 컨트롤러
// Base URL: /api/admin/applies
@RestController
@RequestMapping("/api/admin/applies")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Slf4j
public class AdminApplyController {

    private final AdminApplyService adminApplyService;

    // 모든 지원서 리스트 조회 
    // 예: /api/admin/applies?page=0&size=10
    // 모든 지원서 리스트 조회
    @GetMapping
    public ResponseEntity<?> getAllApplies(Pageable pageable) {
        try {
            // 1. 서비스에서 이제 List를 반환합니다.
            List<AdminApplyListResponse> response = adminApplyService.getAllApplies(pageable);

            // 2. 운영진이 요구한 JSON 포맷으로 구성 (Map 이용)
            return ResponseEntity.ok(java.util.Map.of(
                    "success", true,
                    "message", "지원서 목록을 성공적으로 불러왔습니다.",
                    "data", response
            ));

        } catch (Exception e) {
            log.error("지원서 목록 조회 중 오류 발생", e);
            return buildErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "지원서 목록 조회 중 서버 오류가 발생했습니다."
            );
        }
    }

    // 특정 지원서 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getApplyDetail(@PathVariable Long id) {
        try {

            AdminApplyDetailResponse response =
                    adminApplyService.getApplyDetail(id);

            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException e) {

            return buildErrorResponse(
                    HttpStatus.NOT_FOUND,
                    e.getMessage()
            );

        } catch (Exception e) {

            log.error("지원서 상세 조회 중 오류 발생. id={}", id, e);

            return buildErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "지원서 조회 중 서버 오류가 발생했습니다."
            );
        }
    }

    // 공통 에러 응답 생성 메서드
    private ResponseEntity<ErrorResponse> buildErrorResponse(
            HttpStatus status,
            String message
    ) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .build();

        return ResponseEntity.status(status).body(error);
    }

    // 에러 응답 DTO
    @lombok.Builder
    @lombok.Getter
    private static class ErrorResponse {
        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String message;
    }
}