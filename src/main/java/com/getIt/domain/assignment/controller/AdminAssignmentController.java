package com.getit.domain.assignment.controller;

import com.getit.domain.assignment.dto.FileDownloadDto;
import com.getit.domain.assignment.dto.AssignmentFeedbackCreateRequestDto;
import com.getit.domain.assignment.dto.AssignmentFeedbackResponseDto;
import com.getit.domain.assignment.dto.AssignmentFeedbackUpdateRequestDto;
import com.getit.domain.assignment.dto.AdminAssignmentDetailResponse;
import com.getit.domain.assignment.dto.AdminAssignmentListResponse;
import com.getit.domain.assignment.service.AdminAssignmentService;
import com.getit.domain.assignment.service.AssignmentFeedbackService;
import jakarta.validation.Valid;
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
import java.util.List;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin/assignments")
@RequiredArgsConstructor
public class AdminAssignmentController {

    private final AdminAssignmentService adminAssignmentService;
    private final AssignmentFeedbackService assignmentFeedbackService;

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

    // ===== 과제(Assignment) 관리자 코멘트(이력) =====

    @GetMapping("/{assignmentId}/feedbacks")
    public ResponseEntity<List<AssignmentFeedbackResponseDto>> getAssignmentFeedbacks(@PathVariable Long assignmentId) {
        return ResponseEntity.ok(assignmentFeedbackService.getFeedbacksForAdmin(assignmentId));
    }

    @PostMapping("/{assignmentId}/feedbacks")
    public ResponseEntity<AssignmentFeedbackResponseDto> createAssignmentFeedback(
            @PathVariable Long assignmentId,
            @Valid @RequestBody AssignmentFeedbackCreateRequestDto request
    ) {
        return ResponseEntity.status(201).body(assignmentFeedbackService.createFeedback(assignmentId, request));
    }

    @PatchMapping("/feedbacks/{feedbackId}")
    public ResponseEntity<AssignmentFeedbackResponseDto> updateAssignmentFeedback(
            @PathVariable Long feedbackId,
            @Valid @RequestBody AssignmentFeedbackUpdateRequestDto request
    ) {
        return ResponseEntity.ok(assignmentFeedbackService.updateFeedback(feedbackId, request));
    }

    @DeleteMapping("/feedbacks/{feedbackId}")
    public ResponseEntity<Void> deleteAssignmentFeedback(@PathVariable Long feedbackId) {
        assignmentFeedbackService.deleteFeedback(feedbackId);
        return ResponseEntity.noContent().build();
    }
}