package com.getit.domain.admin.assignment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer week;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskType type;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDateTime deadline;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Task : Assignment = 1 : N
    @Builder.Default
    @OneToMany(mappedBy = "task")
    private List<Assignment> assignments = new ArrayList<>();

}