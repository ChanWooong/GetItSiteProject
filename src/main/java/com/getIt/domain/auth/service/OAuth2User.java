package com.getit.domain.auth.service;

import com.getit.domain.member.entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class OAuth2User extends DefaultOAuth2User {

    private final Member member;

    public OAuth2User(Collection<? extends GrantedAuthority> authorities,
                      Map<String, Object> attributes,
                      String nameAttributeKey,
                      Member member) {
        super(authorities, attributes, nameAttributeKey);
        this.member = member;
    }

    public Long getMemberId() {
        return member.getId();
    }
}