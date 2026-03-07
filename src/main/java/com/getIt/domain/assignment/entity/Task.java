package com.getit.domain.assignment.entity;

import com.getit.domain.assignment.Status;
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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "task", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"week", "type"})
})
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


    @Builder
    private Task(Integer week, TaskType type, String title, String description, LocalDateTime deadline) {
        this.week = week;
        this.type = type;
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
}
