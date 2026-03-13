package com.getit.domain.lecture.entity;

import com.getit.domain.assignment.TrackType;
import com.getit.domain.assignment.entity.Task;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_id", updatable = false, nullable = false)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 255)
    private String title;

    @NotBlank
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @NotNull
    @Column(nullable = false)
    private Integer week;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrackType type;

    // 영상 없는 경우도 있음
    @Nullable
    @Column(nullable = true)
    private String videoUrl;

    // 강의 자료
    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LectureFile> lectureFiles = new ArrayList<>();

    // lecture가 먼저 생성 된 이후 task 생성?
    // lecture가 생성 되는 시점에 task가 미리 존재해 있는 것이 과연 자연스러운가?
    // 일단 nullable
    @OneToOne(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    private Task task;

    // qna 구현 완료 후 사용
    // @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<Qna> qnas;

    @Builder
    private Lecture(Task task, String title, String description, TrackType type, Integer week, String videoUrl) {
        this.task = task;
        this.title = title;
        this.description = description;
        this.type = type;
        this.week = week;
        this.videoUrl = videoUrl;
    }

    // 강의 자료 추가를 위한 연관관계 편의 메서드
    public void addLectureFile(LectureFile file) {
        this.lectureFiles.add(file);
    }
}
