package com.getit.domain.assignment.repository;

import com.getit.domain.assignment.entity.Assignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    @EntityGraph(attributePaths = {"task", "task.lecture", "assignmentFiles"})
    @Query("SELECT a FROM Assignment a WHERE a.member.id = :memberId")
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

    @EntityGraph(attributePaths = {"task", "task.lecture"})
    @Query("SELECT a FROM Assignment a")
    Page<Assignment> findAllWithTaskAndLecture(Pageable pageable);

    @EntityGraph(attributePaths = {"member.memberInfo", "task", "assignmentFiles"})
    Optional<Assignment> findByTaskLectureIdAndMemberId(Long lectureId, Long memberId);


    // 특정 Task의 과제 제출 목록 조회
    List<Assignment> findByTaskId(Long taskId);


    // 특정 Member가 제출한 과제 조회
    List<Assignment> findByMemberId(Long memberId);

}
