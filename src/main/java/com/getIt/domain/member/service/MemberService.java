package com.getit.domain.member.service;

import com.getit.domain.apply.repository.ApplicationRepository;
import com.getit.domain.member.dto.MemberInfoRequest;
import com.getit.domain.member.dto.MemberResponse;
import com.getit.domain.member.entity.Member;
import com.getit.domain.member.Role;
import com.getit.domain.member.entity.MemberInfo;
import com.getit.domain.member.repository.MemberInfoRepository;
import com.getit.domain.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.getit.global.exception.GlobalExceptionManager.BusinessException;
import com.getit.global.exception.ErrorCode;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final ApplicationRepository applicationRepository;

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Transactional
    public void approveMember(Long memberId) {
        Member member = findById(memberId);

        if (member.getRole() == Role.ROLE_MEMBER) {
            throw new BusinessException(ErrorCode.ALREADY_APPROVED_MEMBER);
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
        Member member = findById(memberId);
        MemberInfo info = MemberInfo.builder()
                .member(member)
                .name(dto.getName())
                .studentId(dto.getStudentId())
                .college(dto.getCollege())
                .department(dto.getDepartment())
                .cellNum(dto.getCellNum())
                .build();

        memberInfoRepository.save(info);
        member.completeInfo();
    }

    @Transactional
    public void updateMemberRole(Long memberId, String role) {
        Member member = findById(memberId);

        Role newRole;
        try {
            newRole = Role.valueOf(role);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.INVALID_ROLE_UPDATE, "유효하지 않은 역할입니다: " + role);
        }

        member.updateRole(newRole);
    }
    @Transactional
    public void deleteMember(Long memberId) {
        Member member = findById(memberId);
        applicationRepository.deleteByMember(member);
        memberRepository.delete(member);
    }
    public List<MemberResponse> findAllMembers() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::from)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public MemberResponse getMemberInfo(Long memberId){
        Member member = findById(memberId);
        return MemberResponse.from(member);
    }
    @Transactional
    public void updateMemberInfo(Long memberId, MemberInfoRequest requestDto){
        Member member = findById(memberId);
        MemberInfo memberInfo = member.getMemberInfo();
        if(memberInfo == null){
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "등록된 상세 정보가 없습니다.");        }
        memberInfo.updateInfo(
                requestDto.getName(),
                requestDto.getStudentId(),
                requestDto.getCollege(),
                requestDto.getDepartment(),
                requestDto.getCellNum()
        );
    }

}