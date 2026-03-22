package com.getit.domain.lecture.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Table(name = "lecture_file")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LectureFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", nullable = false, updatable = false)
    private Lecture lecture;

    @NotBlank
    @Column(nullable = false)
    private String fileName;

    @NotBlank
    @Column(nullable = false)
    private String filePath;

    @Builder
    private LectureFile(Lecture lecture, String fileName, String filePath) {
        this.lecture = lecture;
        this.fileName = fileName;
        this.filePath = filePath;
    }

}
