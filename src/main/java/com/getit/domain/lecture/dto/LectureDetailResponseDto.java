package com.getit.domain.lecture.dto;

import com.getit.domain.assignment.TrackType;
import com.getit.domain.lecture.entity.Lecture;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LectureDetailResponseDto {

    private Long lectureId;
    private String title;
    private String description;
    private Integer week;
    private TrackType type;
    private String videoUrl;
    private String resourceUrl;

    public static LectureDetailResponseDto from(Lecture lecture) {
        return LectureDetailResponseDto.builder()
                .lectureId(lecture.getId())
                .title(lecture.getTitle())
                .description(lecture.getDescription())
                .week(lecture.getWeek())
                .type(lecture.getType())
                .videoUrl(lecture.getVideoUrl())
                .resourceUrl(lecture.getResourceUrl())
                .build();
    }
}
