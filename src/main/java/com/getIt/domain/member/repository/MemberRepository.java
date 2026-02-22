package com.getit.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.getit.domain.member.entity.Member;
import com.getit.domain.member.Role;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    List<Member> findAllByRoleAndHasInfoTrue(Role role);
}