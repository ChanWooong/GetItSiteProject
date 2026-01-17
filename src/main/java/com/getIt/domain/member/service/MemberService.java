package com.getit.domain.member.service;

import com.getit.domain.member.entity.Member;
import com.getit.domain.member.Role;
import com.getit.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    // 회원가입 부분 -> 중복성 검사, 멤버 빌드해서 저장하는 과정까지.
    @Transactional
    public Long signUp(Long studentId, String rawPassword, String name, String track) {
        if (memberRepository.existsByStudentId(studentId)) {
            throw new IllegalArgumentException("이미 가입된 학번입니다.");
        }

        Member member = Member.builder()
                .studentId(studentId)
                .password(rawPassword)
                .name(name)
                .role(Role.ROLE_MEMBER)
                .build();

        memberRepository.save(member);
        return member.getId();
    }

    //로그인 부분 -> 유효한 학번인지, 그리고 비밀번호가 맞는지 체크하는 역할을 하는 부분.
    public Member login(Long studentId, String password) {
        Member member = (Member) memberRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학번입니다."));
        if (!member.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        return member;
    }
}