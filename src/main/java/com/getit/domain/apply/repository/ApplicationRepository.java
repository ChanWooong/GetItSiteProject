package com.getit.domain.apply.repository;

import com.getit.domain.apply.entity.Application;
import com.getit.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findAllByMemberOrderByIdDesc(Member member);

    Optional<Application> findFirstByMemberAndIsDraftTrueOrderByIdDesc(Member member);

    void deleteByMemberAndIsDraftTrue(Member member);

    void deleteByMemberAndIsDraftTrueAndIdNot(Member member, Long id);

    boolean existsByMemberAndIsDraftFalse(Member member);

    void deleteByMember(Member member);

}
