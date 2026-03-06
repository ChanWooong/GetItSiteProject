package com.getit.domain.apply.controller;

import com.getit.domain.apply.service.RecruitmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/recruitment")
@RequiredArgsConstructor
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    @GetMapping("/status")
    public ResponseEntity<?> getStatus() {
        return ResponseEntity.ok(Map.of("isOpen", recruitmentService.isOpen()));
    }

    // 모집 기간 설정 (관리자)
    @PatchMapping("/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updatePeriod(@RequestBody Map<String, String> body) {
        LocalDateTime startAt = LocalDateTime.parse(body.get("startAt"));
        LocalDateTime endAt = LocalDateTime.parse(body.get("endAt"));
        recruitmentService.updatePeriod(startAt, endAt);
        return ResponseEntity.ok(Map.of("success", true));
    }
}