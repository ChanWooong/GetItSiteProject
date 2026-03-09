package com.getit.domain.assignment.repository;

import com.getit.domain.assignment.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    @Query("SELECT DISTINCT a FROM Assignment a " +
            "JOIN FETCH a.task " +
            "LEFT JOIN FETCH a.assignmentFiles " +
            "WHERE a.member.id = :memberId")
    List<Assignment> findAllByMemberIdWithTaskAndFiles(@Param("memberId") Long memberId);
}
