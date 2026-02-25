package com.getit.domain.assignment.entity;

import com.getit.domain.assignment.TaskType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "task")
@DynamicUpdate
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id", updatable = false, nullable = false)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Integer week;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskType type;

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

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    private Task(Integer week, TaskType type, String title, String description, LocalDateTime deadline) {
        this.week = week;
        this.type = type;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
    }

    public void updateTask(Integer week, TaskType type, String title, String description, LocalDateTime deadline) {
        if (week == null && type == null  && title == null && description == null && deadline == null) {
            throw new IllegalArgumentException("업데이트 할 데이터가 없습니다.");
        }

        if (week != null) {
            this.week = week;
        }
        if (type != null) {
            this.type = type;
        }
        if (title != null && !title.isBlank()) {
            this.title = title;
        }
        if (description != null) {
            this.description = description;
        }
        if (deadline != null) {
            this.deadline = deadline;
        }
    }
}
