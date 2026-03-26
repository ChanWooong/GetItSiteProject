package com.getit.domain.assignment.controller;

import com.getit.domain.assignment.dto.*;
import com.getit.domain.assignment.service.AssignmentService;
import com.getit.domain.auth.dto.PrincipalDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MEMBER')")
public class AssignmentController {

    private final AssignmentService assignmentService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AssignmentTemporaryResponse<AssignmentResultDto>> createAssignment(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @RequestPart(value = "request") @Valid AssignmentSubmitRequest request
            ) {
        AssignmentResultDto result = assignmentService
                .createAssignment(principalDetails.getMember().getId(), files, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(AssignmentTemporaryResponse.success(
                        result.getFailedFiles().isEmpty()
                        ? "모든 과제 파일이 성공적으로 제출되었습니다."
                        : "일부 파일 업로드에 성공했습니다.",
                        result
                ));
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AssignmentTemporaryResponse<AssignmentUpdateResultDto>> updateAssignment(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @PathVariable Long id,
            @RequestPart(value = "request", required = false) @Valid AssignmentUpdateRequest request
    ) {
        boolean hasFiles = files != null && !files.isEmpty() && !files.get(0).isEmpty();
        boolean hasRequest = request != null &&
                (request.getComment() != null ||
                        request.getGithubUrl() != null ||
                        (request.getDeletedFiles() != null && !request.getDeletedFiles().isEmpty()));
        if (!hasFiles && !hasRequest) {
            throw new IllegalArgumentException("수정할 내용이 없습니다.");
        }

        AssignmentUpdateResultDto result =
                assignmentService.updateAssignment(principalDetails.getMember().getId(), files, id, request);
        boolean noFailedFiles = result.getFailedFiles() == null || result.getFailedFiles().isEmpty();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(AssignmentTemporaryResponse.success(
                        noFailedFiles
                        ? "모든 수정 사항이 성공적으로 반영되었습니다."
                        : "일부 수정사항만 반영되었습니다.",
                        result
                ));
    }

    @GetMapping("/me")
    public ResponseEntity<AssignmentTemporaryResponse<List<AssignmentReadResultDto>>> getMyAssignments(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        List<AssignmentReadResultDto> result = assignmentService.getAssignments(principalDetails.getMember().getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(AssignmentTemporaryResponse.success(
                        "과제 제출 내역을 조회했습니다.",
                        result
                ));
    }

    @GetMapping("/files/{fileId}/download")
    public ResponseEntity<Resource> downloadMyAssignmentFile(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long fileId
    ) {
        FileDownloadDto downloadDto = assignmentService.downloadFileForMember(principalDetails.getMember().getId(), fileId);
        ContentDisposition contentDisposition = ContentDisposition.attachment()
                .filename(downloadDto.originFileName(), StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .body(downloadDto.resource());
    }

}

