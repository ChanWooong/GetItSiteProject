package com.getit.domain.admin.apply.service;

import com.getit.domain.admin.apply.dto.response.AdminApplyDetailResponse;
import com.getit.domain.admin.apply.dto.response.AdminApplyListResponse;

import java.util.List;

//  관리자 전용 지원서 조회 서비스 인터페이스
public interface AdminApplyService {

    //  모든 지원서 리스트 조회
    //  - 기본적으로 isDraft = false 인 데이터만 조회
    //  @return 지원서 요약 리스트
    List<AdminApplyListResponse> getAllApplies();

    //  특정 지원서 상세 조회
    //  - 존재하지 않으면 EntityNotFoundException 발생

    //  @param id 지원서 PK
    //  @return 지원서 상세 정보
    //  @throws jakarta.persistence.EntityNotFoundException
    AdminApplyDetailResponse getApplyDetail(Long id);
}