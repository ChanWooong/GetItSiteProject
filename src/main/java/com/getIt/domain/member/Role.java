package com.getit.domain.member; // 👈 본인 패키지 경로에 맞게 수정!

public enum Role {
    ROLE_GUEST,
    ROLE_MEMBER,
    ROLE_ADMIN;

    public String getKey() {
        return name();
    }
}