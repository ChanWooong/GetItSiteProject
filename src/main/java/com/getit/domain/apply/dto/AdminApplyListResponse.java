package com.getit.domain.apply.dto;

import lombok.Builder;
import lombok.Getter;

//  관리자 지원서 목록 조회용 DTO (요약 정보)
//  - 리스트 화면에서 사용
//  - 불필요한 answer 내용은 제외
@Getter
@Builder
public class AdminApplyListResponse {

    //  지원서 PK
    private final Long id;

    //  지원자(Member) ID
    private final Long memberId;

    private String name;

    private String department;

    //  임시 저장 여부
    private final Boolean isDraft;
}