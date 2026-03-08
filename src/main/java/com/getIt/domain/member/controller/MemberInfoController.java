package com.getit.domain.member.controller;

import com.getit.domain.auth.dto.PrincipalDetails;
import com.getit.domain.member.dto.MemberInfoRequest;
import com.getit.domain.member.dto.MemberResponse;
import com.getit.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberInfoController {

    private final MemberService memberService;

    @PostMapping("/info")
    public ResponseEntity<String> registerMemberInfo(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Valid @RequestBody MemberInfoRequest requestDto) {
        memberService.saveMemberInfo(principalDetails.getMember().getId(), requestDto);
        return ResponseEntity.ok("추가 정보 등록이 완료되었습니다.");
    }

    @GetMapping("/info")
    public ResponseEntity<MemberResponse> getMemberInfo(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        MemberResponse response = memberService.getMemberInfo(principalDetails.getMember().getId());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/info")
    public ResponseEntity<String> updateMemberInfo(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Valid @RequestBody MemberInfoRequest requestDto) {
        memberService.updateMemberInfo(principalDetails.getMember().getId(), requestDto);
        return ResponseEntity.ok("회원정보가 수정되었습니다.");
    }
}
