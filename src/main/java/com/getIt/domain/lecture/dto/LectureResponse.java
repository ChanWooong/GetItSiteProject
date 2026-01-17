package com.getit.domain.lecture.dto;

import com.getit.domain.lecture.entity.Lecture;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LectureResponse {

    private Long id;
    private Integer week;
    private String title;
    //private String videoUrl;
    private String status;

    // Service에서 Entity와 상태값을 받아 DTO로 변환하는 생성자
    public LectureResponse(Lecture lecture, String status) {
        this.id = lecture.getId();
        this.week = lecture.getWeek();
        this.title = lecture.getTitle();
        //this.videoUrl = lecture.getVideoUrl();
        this.status = status;
    }
}