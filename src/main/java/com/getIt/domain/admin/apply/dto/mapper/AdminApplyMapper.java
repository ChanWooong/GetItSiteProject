package com.getit.domain.admin.apply.dto.mapper;

import com.getit.domain.admin.apply.dto.response.AdminApplyDetailResponse;
import com.getit.domain.admin.apply.dto.response.AdminApplyListResponse;
import com.getit.domain.admin.apply.entity.Application;

import org.springframework.stereotype.Component;

//  Application → Admin DTO 변환 담당 Mapper
@Component
public class AdminApplyMapper {

    //  목록 조회용 DTO 변환
    public AdminApplyListResponse toListResponse(Application application) {

        return AdminApplyListResponse.builder()
                .id(application.getId())
                .memberId(application.getMemberId())
                .isDraft(application.getIsDraft())
                .build();
    }

    //  상세 조회용 DTO 변환
    public AdminApplyDetailResponse toDetailResponse(Application application) {

        return AdminApplyDetailResponse.builder()
                .id(application.getId())
                .memberId(application.getMemberId())
                .answers(application.getAnswerList())
                .isDraft(application.getIsDraft())
                .build();
    }
}