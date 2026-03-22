package com.getit.domain.qna.repository;

import com.getit.domain.qna.entity.QnaAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface QnaAnswerRepository extends JpaRepository<QnaAnswer, Long> {

    // 특정 질문들의 답변 전체 (시간순)
    List<QnaAnswer> findByQnaIdInOrderByCreatedAtAsc(List<Long> qnaIds);

    // 특정 질문들 중 가장 최신 답변
    Optional<QnaAnswer> findTopByQnaIdInOrderByCreatedAtDesc(List<Long> qnaIds);
}