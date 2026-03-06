package com.getit.global.jwt; 

import com.getit.domain.member.entity.Member;
import com.getit.domain.member.Role; 
import com.getit.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@Profile({"local","test"}) 
public class TestTokenController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    //  테스트용 관리자 계정 생성 API
    //  - local / test 환경에서만 동작
    //  - DB에 관리자 계정을 생성
    @GetMapping("/create-admin")
    public String createAdmin() {

        // 임시 관리자 생성
        Member admin = Member.builder()
                .email("admin@test.com")
                .role(Role.ROLE_ADMIN)
                .hasInfo(true)
                .isApproved(true)
                .build();

        // DB 저장
        Member savedAdmin = memberRepository.save(admin);

        // 생성된 관리자 ID 반환
        return "관리자 계정이 성공적으로 생성되었습니다. DB에 저장된 회원 ID: " + savedAdmin.getId();
    }

    //  테스트용 JWT 토큰 발급 API
    //  - local / test 환경에서만 동작
    //  - memberId 기반으로 AccessToken 생성
    @GetMapping("/token/{memberId}")
    public String getTestToken(@PathVariable Long memberId) {

        // 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "DB에 ID가 " + memberId + "인 회원이 없습니다."
                ));

        // JWT 토큰 발급
        return jwtTokenProvider.createAccessToken(member);
    }
}