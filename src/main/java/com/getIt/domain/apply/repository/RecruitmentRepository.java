package com.getit.domain.apply.repository;

import com.getit.domain.apply.entity.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {
    Optional<Recruitment> findTopByOrderByIdDesc();
}