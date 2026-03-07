package com.getit.domain.admin.assignment.controller;

import com.getit.domain.admin.assignment.dto.response.AdminAssignmentListResponse;
import com.getit.domain.admin.assignment.service.AdminAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/assignments")
@RequiredArgsConstructor
public class AdminAssignmentController {

    private final AdminAssignmentService adminAssignmentService;

    // 부원들이 제출한 전체 과제 조회
    // GET /api/admin/assignments/all?page=0&size=10
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<Page<AdminAssignmentListResponse>> getAllAssignments(
            Pageable pageable
    ) {

        Page<AdminAssignmentListResponse> assignments =
                adminAssignmentService.getAllAssignments(pageable);

        return ResponseEntity.ok(assignments);
    }
}