package com.getit.domain.assignment.entity;

import com.getit.domain.assignment.Status;
import com.getit.domain.member.entity.Member;

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
import java.util.Objects;

@Entity
@Table(name = "assignment", uniqueConstraints = {
    @UniqueConstraint(
        name = "uk_task_member",
        columnNames = {"task_id", "member_id"}
    )
})
@Getter
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false, updatable = false)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false,  updatable = false)
    private Member member;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column
    private Status status;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @NotBlank
    @Column(nullable = false)
    private String dirName;

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssignmentFile> assignmentFiles = new ArrayList<>();

    @CreatedDate
    @Column(name = "submitted_at", updatable = false, nullable = false)
    private LocalDateTime submittedAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    @Builder
    private Assignment(Task task, Member member, Status status, String dirName, String comment) {
        this.task = task;
        this.member = member;
        this.status = status;
        this.dirName = dirName;
        this.comment = comment;
    }

    public void updateStatus(Status status) {
        this.status = Objects.requireNonNull(status, "status는 null일 수 없습니다.");
    }

    public void updateComment(String comment) {
        if (comment != null) {
            this.comment = comment;
        }
    }

    public void addAssignmentFile(AssignmentFile file) {
        this.assignmentFiles.add(file);
    }
}
