package com.getit.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.getit.domain.member.entity.Member;
import com.getit.domain.member.entity.Role;
import java.util.Optional;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByStudentId(Long studentId);

    boolean existsByStudentId(Long studentId);

    List<Member> findByTrack(String track);

    List<Member> findByRole(Role role);
}