package com.getit.domain.member.repository;

import com.getit.domain.member.Role;
import com.getit.domain.member.SocialType;
import com.getit.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByEmailAndSocialType(String socialId, SocialType socialType);

    List<Member> findAllByRoleAndHasInfoTrue(Role role);
}