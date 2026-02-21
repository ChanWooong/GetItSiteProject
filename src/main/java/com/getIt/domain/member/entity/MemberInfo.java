package com.getit.domain.member.entity;

import jakarta.persistence.*;

@Entity
public class MemberInfo {
    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String name;
    private String studentId;
    private String collage;
    private String department;
    private String cellNum;
}