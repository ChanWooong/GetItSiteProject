package com.getit.domain.apply.dto;

import com.getit.domain.apply.entity.Application;
import com.getit.domain.member.entity.MemberInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ApplyDraftDataDto {

    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String answer5;
    private String answer6;
    private String answer7;
    private Boolean agree;

    //  Application → Admin DTO 변환 담당 Mapper
    @Component
    public static class AdminApplyMapper {

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
}
