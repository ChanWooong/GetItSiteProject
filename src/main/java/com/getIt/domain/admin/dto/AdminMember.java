package com.getit.domain.admin.dto;

import com.getit.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdminMember {
    private Long id;
    private String name;
    private Long studentId;
    private int progressRate;

    public static AdminMember of(Member member, int progressRate) {
        return AdminMember.builder()
                .id(member.getId())
                .name(member.getName())
                .studentId(member.getStudentId())
                .progressRate(progressRate)
                .build();
    }
}