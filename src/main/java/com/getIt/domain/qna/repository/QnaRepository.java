package com.getit.domain.qna.repository;

import com.getit.domain.qna.entity.Qna;
import com.getit.domain.qna.entity.QnaAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QnaRepository extends JpaRepository<Qna, Long> {

    List<Qna> findByLectureIdOrderByCreatedAtDesc(Long lectureId);

    @Query("""
    SELECT q
    FROM Qna q
    JOIN FETCH q.member m
    WHERE q.lecture.id = :lectureId
      AND m.id = :memberId
    ORDER BY q.createdAt ASC
    """)
    List<Qna> findByLectureIdAndMemberIdOrderByCreatedAtAsc(
            @Param("lectureId") Long lectureId,
            @Param("memberId") Long memberId
    );
}