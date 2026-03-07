package com.getit.domain.admin.assignment.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "assignment_file")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Assignment와 N:1 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path", nullable = false)
    private String filePath;
}