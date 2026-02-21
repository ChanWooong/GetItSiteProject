package com.getit.domain.auth.service;

import com.getit.domain.auth.dto.*;
import com.getit.domain.member.entity.Member;
import com.getit.domain.member.Role;
import com.getit.domain.member.repository.MemberRepository;
import com.getit.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void signup(SignupRequest request) {
        if (memberRepository.existsByStudentId(request.getStudentId())) {
            throw new IllegalArgumentException("이미 가입된 학번입니다.");
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Member member = Member.builder()
                .studentId(request.getStudentId())
                .password(encodedPassword)
                .name(request.getName())
                .role(Role.ROLE_GUEST)
                .isApproved(false)
                .build();

        memberRepository.save(member);
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        Member member = memberRepository.findByStudentId(request.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        String accessToken = jwtTokenProvider.createAccessToken(
                member.getStudentId().toString(),
                member.getRole().name()
        );
        String refreshToken = jwtTokenProvider.createRefreshToken(
                member.getStudentId().toString()
        );
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(member.getRole().name())
                .name(member.getName())
                .build();
    }
}

