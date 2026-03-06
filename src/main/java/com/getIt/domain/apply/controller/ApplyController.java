package com.getit.domain.apply.controller;

import com.getit.domain.auth.dto.PrincipalDetails;
import com.getit.domain.apply.dto.ApplyDraftLoadResponse;
import com.getit.domain.apply.dto.ApplyDraftRequest;
import com.getit.domain.apply.dto.ApplyRequest;
import com.getit.domain.apply.dto.ApplyResponse;
import com.getit.domain.apply.service.ApplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/applies")
@RequiredArgsConstructor
public class ApplyController {

    private final ApplyService applyService;

    @PostMapping
    public ResponseEntity<ApplyResponse> submit(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Valid @RequestBody ApplyRequest request) {
        applyService.submit(principalDetails.getMember().getId(), request);
        return ResponseEntity.ok(ApplyResponse.success("지원서가 제출되었습니다."));
    }

    @PutMapping("/draft")
    public ResponseEntity<ApplyResponse> saveDraft(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody ApplyDraftRequest request) {
        applyService.saveDraft(principalDetails.getMember().getId(), request);
        return ResponseEntity.ok(ApplyResponse.success("작성 중인 내용이 임시 저장되었습니다."));
    }

    @GetMapping("/draft")
    public ResponseEntity<ApplyDraftLoadResponse> getDraft(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return applyService.getDraft(principalDetails.getMember().getId())
                .map(data -> ResponseEntity.ok(
                        ApplyDraftLoadResponse.of("임시 저장된 데이터를 불러왔습니다.", data)))
                .orElse(ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND)
                        .body(ApplyDraftLoadResponse.notFound("임시 저장된 데이터가 없습니다.")));
    }
    @GetMapping("/me")
    public ResponseEntity<ApplyResponse> checkSubmitted(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        boolean submitted = applyService.hasSubmitted(principalDetails.getMember().getId());
        return ResponseEntity.ok(ApplyResponse.of(submitted));
    }
}
