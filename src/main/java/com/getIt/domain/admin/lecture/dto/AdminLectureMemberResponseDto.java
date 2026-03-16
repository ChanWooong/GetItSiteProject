package com.getit.domain.admin.lecture.dto;

import com.getit.domain.assignment.TrackType;
import com.getit.domain.lecture.entity.Lecture;
import com.getit.domain.member.entity.Member;
import com.getit.domain.member.entity.MemberInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminLectureMemberResponseDto {

    // Lecture info
    private Long lectureId;
    private String title;
    private String description;
    private Integer week;
    private TrackType type;
    private String videoUrl;

    // Member info
    private Long memberId;
    private String email;

    // MemberInfo
    private String name;
    private String studentId;
    private String department;
    private String cellNum;

    public static AdminLectureMemberResponseDto of(Lecture lecture, Member member) {
        MemberInfo memberInfo = member.getMemberInfo();

        AdminLectureMemberResponseDtoBuilder builder = AdminLectureMemberResponseDto.builder()
                .lectureId(lecture.getId())
                .title(lecture.getTitle())
                .description(lecture.getDescription())
                .week(lecture.getWeek())
                .type(lecture.getType())
                .videoUrl(lecture.getVideoUrl())
                .memberId(member.getId())
                .email(member.getEmail());

        if (memberInfo != null) {
            builder.name(memberInfo.getName())
                    .studentId(memberInfo.getStudentId())
                    .department(memberInfo.getDepartment())
                    .cellNum(memberInfo.getCellNum());
        }

        return builder.build();
    }
}
