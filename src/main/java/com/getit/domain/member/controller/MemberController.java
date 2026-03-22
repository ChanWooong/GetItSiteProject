package com.getit.domain.member.controller;

import com.getit.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getPendingMembers() {
        return ResponseEntity.ok(memberService.findPendingMembers());
    }


    @PatchMapping("/{memberId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> approveMember(@PathVariable Long memberId) {
        memberService.approveMember(memberId);
        return ResponseEntity.ok("멤버 승인이 완료되었습니다.");
    }
    @PatchMapping("/{memberId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateMemberRole(
            @PathVariable Long memberId,
            @RequestBody java.util.Map<String, String> body) {
        memberService.updateMemberRole(memberId, body.get("role"));
        return ResponseEntity.ok("역할이 변경되었습니다.");
    }

    @DeleteMapping("/{memberId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteMember(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.ok("회원이 삭제되었습니다.");
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllMembers() {
        return ResponseEntity.ok(memberService.findAllMembers());
    }
}