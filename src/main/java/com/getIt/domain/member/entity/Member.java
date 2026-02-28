package com.getit.domain.member.entity;

import com.getit.domain.member.Role;
import com.getit.domain.member.SocialType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String socialId;

    @Enumerated(EnumType.STRING)
    private SocialType socialType; // GOOGLE, KAKAO 등 저장

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // ROLE_GUEST, ROLE_MEMBER, ROLE_ADMIN

    @Column(nullable = false)
    private boolean hasInfo; // 추가 정보(학번 등) 입력 여부

    @Builder.Default
    @Column(nullable = false)
    private boolean isApproved = false;

    // 1:1 관계 설정 (MemberInfo와 연결)
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // 불필요한 조인을 방지하기 위해.
    private MemberInfo memberInfo;

    public void completeInfo() {
        this.hasInfo = true;
    }
    public void registMember() {
        this.role = Role.ROLE_MEMBER;
    }
    public Member updateEmail(String email) {
        this.email = email;
        return this;
    }
}