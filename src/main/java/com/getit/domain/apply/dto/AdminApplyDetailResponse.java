package com.getit.domain.apply.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

//  관리자 지원서 상세 조회용 DTO
@Getter
@Builder
public class AdminApplyDetailResponse {

    //  지원서 PK
    private final Long id;

    //  지원자(Member) ID
    private final Long memberId;

    private final List<String> answers;
    //  임시 저장 여부
    private final Boolean isDraft;

    private final String name;

    private final String department;
}