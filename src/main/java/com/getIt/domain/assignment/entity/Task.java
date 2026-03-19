package com.getit.domain.assignment.entity;

import com.getit.domain.assignment.Status;
import com.getit.domain.lecture.entity.Lecture;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "tasks",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_tasks_lecture_id", columnNames = "lecture_id")
    }
)
@DynamicUpdate
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Lecture : Task = 1 : N 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", nullable = false)
    private Lecture lecture;

    @NotBlank
    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private LocalDateTime deadline;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    private Task(Lecture lecture, String title, String description, LocalDateTime deadline) {
        this.lecture = lecture;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
    }

    public Status determineSubmitStatus(LocalDateTime submittedAt) {
        if (deadline != null && submittedAt.isAfter(deadline)) {
            return Status.LATE;
        } else {
            return Status.SUBMITTED;
        }
    }

    /** PATCH용 부분 수정. title/description에 빈 문자열은 허용하지 않음. */
    public void update(String title, String description, LocalDateTime deadline) {
        if (title != null) {
            if (title.isBlank()) {
                throw new IllegalArgumentException("제목은 비어 있을 수 없습니다.");
            }
            this.title = title;
        }
        if (description != null) {
            if (description.isBlank()) {
                throw new IllegalArgumentException("설명은 비어 있을 수 없습니다.");
            }
            this.description = description;
        }
        if (deadline != null) {
            this.deadline = deadline;
        }
    }
}