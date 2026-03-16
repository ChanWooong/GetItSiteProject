package com.getit.domain.lecture.dto;

import com.getit.domain.lecture.entity.Lecture;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LectureResponseDto {

    private Long lectureId;
    private String title;

    public static LectureResponseDto from(Lecture lecture) {
        return LectureResponseDto.builder()
                .lectureId(lecture.getId())
                .title(lecture.getTitle())
                .build();
    }
}
