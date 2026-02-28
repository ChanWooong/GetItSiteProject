package com.getit.domain.member.service;

import com.getit.domain.member.dto.MemberInfoRequest;
import com.getit.domain.member.dto.MemberResponse;
import com.getit.domain.member.entity.Member;
import com.getit.domain.member.Role;
import com.getit.domain.member.entity.MemberInfo;
import com.getit.domain.member.repository.MemberInfoRepository;
import com.getit.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberInfoRepository memberInfoRepository;

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }

    @Transactional
    public void approveMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (member.getRole() == Role.ROLE_MEMBER) {
            throw new IllegalStateException("이미 승인된 사용자입니다.");
        }
        member.registMember();
    }

    public List<MemberResponse> findPendingMembers() {
        return memberRepository.findAllByRoleAndHasInfoTrue(Role.ROLE_GUEST).stream()
                .map(MemberResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveMemberInfo(Long memberId, MemberInfoRequest dto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        MemberInfo info = MemberInfo.builder()
                .member(member)
                .name(dto.getName())
                .studentId(dto.getStudentId())
                .college(dto.getCollege())
                .department(dto.getDepartment())
                .cellNum(dto.getCellNum())
                .build();

        memberInfoRepository.save(info);

    }
}