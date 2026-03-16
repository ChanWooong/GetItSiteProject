package com.getit.domain.qna.controller;

import com.getit.domain.auth.dto.PrincipalDetails;
import com.getit.domain.qna.dto.QnaChatMessage;
import com.getit.domain.qna.service.QnaService;
import com.getit.domain.qna.dto.QnaRequest;
import com.getit.domain.qna.dto.QnaRoomResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lecture/{lectureId}/qna")
@RequiredArgsConstructor
public class QnaController {

    private final QnaService qnaService;

    // ── 멤버용 ──────────────────────────────

    // 내 채팅 전체 조회
    @GetMapping("/me")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<List<QnaChatMessage>> getMyChat(
            @PathVariable Long lectureId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity.ok(qnaService.getMyChat(lectureId, principalDetails));
    }

    // 질문 작성
    @PostMapping
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<QnaChatMessage> createQuestion(
            @PathVariable Long lectureId,
            @RequestBody @Valid QnaRequest request,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity.ok(qnaService.createQuestion(lectureId, request, principalDetails));
    }

    // 질문 삭제
    @DeleteMapping("/{qnaId}")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<Void> deleteQuestion(
            @PathVariable Long lectureId,
            @PathVariable Long qnaId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        qnaService.deleteQuestion(lectureId, qnaId, principalDetails);
        return ResponseEntity.noContent().build();
    }

    // ── 관리자용 ─────────────────────────────

    // 채팅방 목록 조회 (미처리 여부 포함)
    @GetMapping("/rooms")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<QnaRoomResponse>> getRooms(
            @PathVariable Long lectureId) {
        return ResponseEntity.ok(qnaService.getRooms(lectureId));
    }

    // 특정 멤버 채팅 전체 조회
    @GetMapping("/rooms/{memberId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<QnaChatMessage>> getMemberChat(
            @PathVariable Long lectureId,
            @PathVariable Long memberId) {
        return ResponseEntity.ok(qnaService.getMemberChat(lectureId, memberId));
    }

    // 답변 작성
    @PostMapping("/{qnaId}/answer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<QnaChatMessage> createAnswer(
            @PathVariable Long lectureId,
            @PathVariable Long qnaId,
            @RequestBody @Valid QnaRequest request) {
        return ResponseEntity.ok(qnaService.createAnswer(lectureId, qnaId, request));
    }

    // 답변 삭제
    @DeleteMapping("/answer/{answerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAnswer(
            @PathVariable Long lectureId,
            @PathVariable Long answerId) {
        qnaService.deleteAnswer(lectureId, answerId);
        return ResponseEntity.noContent().build();
    }
}