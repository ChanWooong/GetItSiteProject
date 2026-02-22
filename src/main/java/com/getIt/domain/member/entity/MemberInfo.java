package com.getit.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberInfo {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private Long studentId;

    @Column(nullable = false)
    private String collage;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String cellNum;

}