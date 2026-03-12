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

    // Admin 전체 과제 제출 조회
    // Task 정보를 함께 조회하기 위해 JOIN FETCH 사용
    @Query("""
            SELECT a
            FROM Assignment a
            JOIN FETCH a.task
            ORDER BY a.submittedAt DESC
            """)
    List<Assignment> findAllWithTask();


    // 특정 Task의 과제 제출 목록 조회
    List<Assignment> findByTaskId(Long taskId);


    // 특정 Member가 제출한 과제 조회
    List<Assignment> findByMemberId(Long memberId);

}
