package com.getit.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import com.getit.domain.member.College;

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

    @Column(nullable = false)
    private String studentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private College college;

    @Column(nullable = false)
    private String department; //너무 많아서 조금 더 고민을 해보는 것이 좋을 듯.

    @Column(nullable = false)
    private String cellNum;

    public void updateInfo(String name, String studentId, College college, String department, String cellNum) {
        this.name = name;
        this.studentId = studentId;
        this.college = college;
        this.department = department;
        this.cellNum = cellNum;
    }
}