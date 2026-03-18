package com.getit.domain.lecture.entity;

import com.getit.domain.assignment.TrackType;
import com.getit.domain.assignment.entity.Task;
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
    @Column(name = "id", updatable = false, nullable = false)
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

    // 영상 존재 여부
    @Column(nullable = false)
    private boolean hasVideo;

    // 영상 URL
    @Column(columnDefinition = "TEXT")
    private String videoUrl;

    // 강의 자료 URL (PDF, PPT 링크)
    @Column(columnDefinition = "TEXT")
    private String resourceUrl;

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LectureFile> lectureFiles = new ArrayList<>();

    @OneToOne(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    private Task task;

    @Builder
    private Lecture(Task task, String title, String description, TrackType type, Integer week, String videoUrl, String resourceUrl) {
        this.task = task;
        this.title = title;
        this.description = description;
        this.type = type;
        this.week = week;

        setVideo(videoUrl);
        setResource(resourceUrl);
    }

    private void setVideo(String videoUrl) {
        if (videoUrl == null || videoUrl.isBlank()) {
            this.hasVideo = false;
            this.videoUrl = null;
        } else {
            this.hasVideo = true;
            this.videoUrl = videoUrl;
        }
    }

    private void setResource(String resourceUrl) {
        if (resourceUrl == null || resourceUrl.isBlank()) {
            this.resourceUrl = null;
        } else {
            this.resourceUrl = resourceUrl;
        }
    }

    public void addLectureFile(LectureFile file) {
        this.lectureFiles.add(file);
    }

    public void update(String title, String description, Integer week, TrackType type, String videoUrl, String resourceUrl) {

        if (title != null) this.title = title;
        if (description != null) this.description = description;
        if (week != null) this.week = week;
        if (type != null) this.type = type;

        if (videoUrl != null) {
            setVideo(videoUrl);
        }

        if (resourceUrl != null) {
            setResource(resourceUrl);
        }
    }
}