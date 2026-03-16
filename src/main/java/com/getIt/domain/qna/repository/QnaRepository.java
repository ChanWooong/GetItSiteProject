package com.getit.domain.qna.repository;

import com.getit.domain.qna.entity.Qna;
import com.getit.domain.qna.entity.QnaAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QnaRepository extends JpaRepository<Qna, Long> {

    List<Qna> findByLectureIdOrderByCreatedAtDesc(Long lectureId);

    List<Qna> findByLectureIdAndMemberIdOrderByCreatedAtAsc(Long lectureId, Long memberId);
}