package com.getit.global.jwt; // 본인 패키지 경로에 맞게 확인!

import com.getit.domain.member.entity.Member;
import com.getit.domain.member.Role; // Role Enum 임포트 확인!
import com.getit.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestTokenController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @GetMapping("/create-admin")
    public String createAdmin() {
        // 임시 관리자 생성
        Member admin = Member.builder()
                .email("admin@test.com")
                .role(Role.ROLE_ADMIN)
                .hasInfo(true)
                .isApproved(true)
                .build();
        
        Member savedAdmin = memberRepository.save(admin);
        
        return "관리자 계정이 성공적으로 생성되었습니다. DB에 저장된 회원 ID: " + savedAdmin.getId();
    }

    // 토큰 발급기
    @GetMapping("/token/{memberId}")
    public String getTestToken(@PathVariable Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "DB에 ID가 " + memberId + "인 회원이 없습니다."
                ));

        return jwtTokenProvider.createAccessToken(member);
    }
}