package com.getit.domain.member.entity;

import com.getit.domain.member.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Users")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private Long studentId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String teamName;
    private LocalDateTime createdAt;

    @Builder
    public Member(Long studentId, String password, String name, Role role) {
        this.studentId = studentId;
        this.password = password;
        this.name = name;
        this.role = role;
    }
}