package com.getit.domain.admin.assignment.controller;

import com.getit.domain.admin.assignment.dto.internal.FileDownloadDto;
import com.getit.domain.admin.assignment.dto.response.AdminAssignmentDetailResponse;
import com.getit.domain.admin.assignment.dto.response.AdminAssignmentListResponse;
import com.getit.domain.admin.assignment.service.AdminAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin/assignments")
@RequiredArgsConstructor
public class AdminAssignmentController {

    private final AdminAssignmentService adminAssignmentService;

    // 부원들이 제출한 전체 과제 조회
    // GET /api/admin/assignments/all?page=0&size=10
    @GetMapping("/all")
    public ResponseEntity<Page<AdminAssignmentListResponse>> getAllAssignments(
            Pageable pageable
    ) {

        Page<AdminAssignmentListResponse> assignments =
                adminAssignmentService.getAllAssignments(pageable);

        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/{lectureId}/{memberId}")
    public ResponseEntity<AdminAssignmentDetailResponse> getAssignmentDetail(
            @PathVariable Long lectureId,
            @PathVariable Long memberId
    ) {
        AdminAssignmentDetailResponse result = adminAssignmentService.getAssignmentDetail(lectureId, memberId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/files/{fileId}/download")
    public ResponseEntity<Resource> downloadAssignmentFile(@PathVariable Long fileId) {
        FileDownloadDto downloadDto = adminAssignmentService.downloadFile(fileId);

        ContentDisposition contentDisposition = ContentDisposition.attachment()
                .filename(downloadDto.originFileName(), StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .body(downloadDto.resource());
    }
}