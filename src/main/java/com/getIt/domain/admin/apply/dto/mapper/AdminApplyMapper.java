package com.getit.domain.admin.apply.dto.mapper;

import com.getit.domain.admin.apply.dto.response.AdminApplyDetailResponse;
import com.getit.domain.admin.apply.dto.response.AdminApplyListResponse;
import com.getit.domain.apply.entity.Application;
import com.getit.domain.member.entity.MemberInfo;

import org.springframework.stereotype.Component;

//  Application → Admin DTO 변환 담당 Mapper
@Component
public class AdminApplyMapper {

    //  목록 조회용 DTO 변환
    public AdminApplyListResponse toListResponse(Application application) {
        MemberInfo memberInfo = application.getMember().getMemberInfo();

        return AdminApplyListResponse.builder()
                .id(application.getId())
                .memberId(application.getMember().getId())
                .isDraft(application.getIsDraft())
                .name(memberInfo != null ? memberInfo.getName() : "정보 없음")
                .department(memberInfo != null ? memberInfo.getDepartment() : "정보 없음")
                .build();
    }

    //  상세 조회용 DTO 변환
    public AdminApplyDetailResponse toDetailResponse(Application application) {

        return AdminApplyDetailResponse.builder()
                .id(application.getId())
                .memberId(application.getMember().getId())
                .answers(application.getAnswerList())
                .isDraft(application.getIsDraft())
                .build();
    }
}