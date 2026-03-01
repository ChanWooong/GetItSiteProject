package com.getit.domain.member.controller;

import com.getit.domain.auth.dto.PrincipalDetails;
import com.getit.domain.member.dto.MemberInfoRequest;
import com.getit.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
