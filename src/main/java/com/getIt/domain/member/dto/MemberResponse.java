package com.getit.domain.member.dto;

import com.getit.domain.member.College;
import com.getit.domain.member.Role;
import com.getit.domain.member.entity.Member;
import com.getit.domain.member.entity.MemberInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberResponse {

    private Long id;
    private String email;
    private Role role;
    private boolean hasInfo;

    private String name;
    private Long studentId;
    private College college;
    private String department;
    private String cellNum;


    public static MemberResponse from(Member member) {
        MemberInfo info = member.getMemberInfo(); // 1:1 관계에서 가져옴

        return MemberResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .role(member.getRole())
                .hasInfo(member.isHasInfo())
                .name(info != null ? info.getName() : null)
                .studentId(info != null ? info.getStudentId() : null)
                .college(info != null ? info.getCollege() : null)
                .department(info != null ? info.getDepartment() : null)
                .cellNum(info != null ? info.getCellNum() : null)
                .build();
    }
}