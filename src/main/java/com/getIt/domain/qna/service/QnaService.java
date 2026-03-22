package com.getit.domain.qna.service;

import com.getit.domain.auth.dto.PrincipalDetails;
import com.getit.domain.lecture.entity.Lecture;
import com.getit.domain.lecture.repository.LectureRepository;
import com.getit.domain.member.entity.Member;
import com.getit.domain.qna.dto.QnaChatMessage;
import com.getit.domain.qna.dto.QnaRequest;
import com.getit.domain.qna.dto.QnaRoomResponse;
import com.getit.domain.qna.entity.Qna;
import com.getit.domain.qna.entity.QnaAnswer;
import com.getit.domain.qna.repository.QnaAnswerRepository;
import com.getit.domain.qna.repository.QnaRepository;
import com.getit.global.exception.ErrorCode;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.getit.global.exception.ErrorCode;
import com.getit.global.exception.GlobalExceptionManager.BusinessException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QnaService {

    private final QnaRepository qnaRepository;
    private final QnaAnswerRepository qnaAnswerRepository;
    private final LectureRepository lectureRepository;

    public List<QnaChatMessage> getMyChat(Long lectureId, PrincipalDetails principalDetails) {
        Long memberId = principalDetails.getMember().getId();

        List<Qna> qnaList = qnaRepository
                .findByLectureIdAndMemberIdOrderByCreatedAtAsc(lectureId, memberId);
        List<Long> qnaIds = qnaList.stream().map(Qna::getId).toList();
        List<QnaAnswer> answers = qnaIds.isEmpty()
                ? Collections.emptyList()
                : qnaAnswerRepository.findByQnaIdInOrderByCreatedAtAsc(qnaIds);

        return QnaChatMessage.merge(qnaList, answers);
    }

    @Transactional
    public QnaChatMessage createQuestion(Long lectureId, QnaRequest request, PrincipalDetails principalDetails) {
        Lecture lecture = getLecture(lectureId);
        Member member = principalDetails.getMember();
        Qna qna = Qna.create(lecture, member, request.getContent());
        return QnaChatMessage.ofQuestion(qnaRepository.save(qna));
    }

    @Transactional
    public void deleteQuestion(Long lectureId, Long qnaId, PrincipalDetails principalDetails) {
        Qna qna = getQna(qnaId);
        validateLecture(qna, lectureId);
        validateOwner(qna, principalDetails);
        qnaRepository.delete(qna);
    }

    public List<QnaRoomResponse> getRooms(Long lectureId) {
        List<Qna> allQnas = qnaRepository.findByLectureIdOrderByCreatedAtDesc(lectureId);

        Map<Long, List<Qna>> qnaByMember = allQnas.stream()
                .collect(Collectors.groupingBy(q -> q.getMember().getId()));

        List<QnaRoomResponse> result = new ArrayList<>();

        for (Map.Entry<Long, List<Qna>> entry : qnaByMember.entrySet()) {
            List<Qna> memberQnas = entry.getValue(); // 최신순 정렬 상태
            List<Long> qnaIds = memberQnas.stream().map(Qna::getId).toList();

            Qna lastQna = memberQnas.get(0); // 가장 최신 질문
            Optional<QnaAnswer> lastAnswer = qnaAnswerRepository
                    .findTopByQnaIdInOrderByCreatedAtDesc(qnaIds);

            // 마지막 메시지가 USER인지 판단
            boolean unanswered = lastAnswer.isEmpty() ||
                    lastQna.getCreatedAt().isAfter(lastAnswer.get().getCreatedAt());

            // 마지막 메시지 내용
            String lastMessage;
            String lastSender;
            LocalDateTime lastMessageAt;

            if (lastAnswer.isPresent() &&
                    lastAnswer.get().getCreatedAt().isAfter(lastQna.getCreatedAt())) {
                lastMessage = lastAnswer.get().getContent();
                lastSender = "ADMIN";
                lastMessageAt = lastAnswer.get().getCreatedAt();
            } else {
                lastMessage = lastQna.getContent();
                lastSender = "USER";
                lastMessageAt = lastQna.getCreatedAt();
            }

            result.add(QnaRoomResponse.builder()
                    .memberId(entry.getKey())
                    .memberName(lastQna.getMember().getMemberInfo() != null ? lastQna.getMember().getMemberInfo().getName() : "Unknown")
                    .lastMessage(lastMessage)
                    .lastSender(lastSender)
                    .lastMessageAt(lastMessageAt)
                    .unanswered(unanswered)
                    .build());
        }

        result.sort(Comparator.comparing(QnaRoomResponse::isUnanswered).reversed()
                .thenComparing(QnaRoomResponse::getLastMessageAt, Comparator.reverseOrder()));

        return result;
    }

    public List<QnaChatMessage> getMemberChat(Long lectureId, Long memberId) {
        List<Qna> qnaList = qnaRepository
                .findByLectureIdAndMemberIdOrderByCreatedAtAsc(lectureId, memberId);

        List<Long> qnaIds = qnaList.stream().map(Qna::getId).toList();
        List<QnaAnswer> answers = qnaIds.isEmpty()
                ? Collections.emptyList()
                : qnaAnswerRepository.findByQnaIdInOrderByCreatedAtAsc(qnaIds);

        return QnaChatMessage.merge(qnaList, answers);
    }

    // 답변 작성
    @Transactional
    public QnaChatMessage createAnswer(Long lectureId, Long qnaId, QnaRequest request) {
        Qna qna = getQna(qnaId);
        validateLecture(qna, lectureId);
        QnaAnswer answer = QnaAnswer.create(qna, request.getContent());
        return QnaChatMessage.ofAnswer(qnaAnswerRepository.save(answer));
    }

    // 답변 삭제
    @Transactional
    public void deleteAnswer(Long lectureId, Long answerId) {
        QnaAnswer answer = qnaAnswerRepository.findById(answerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ANSWER_NOT_FOUND));
       validateLecture(answer.getQna(), lectureId);
        qnaAnswerRepository.delete(answer);
    }

    // ── private 헬퍼 ─────────────────────────

    private Qna getQna(Long qnaId) {
        return qnaRepository.findById(qnaId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ANSWER_NOT_FOUND));
    }

    private Lecture getLecture(Long lectureId) {
        return lectureRepository.findById(lectureId)
                .orElseThrow(() -> new BusinessException(ErrorCode.LECTURE_NOT_FOUND));
    }

    private void validateLecture(Qna qna, Long lectureId) {
        if (!qna.getLecture().getId().equals(lectureId)) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "해당 강의에 속한 질문, 답변이 아닙니다.");
        }
    }

    private void validateOwner(Qna qna, PrincipalDetails principalDetails) {
        if (!qna.getMember().getId().equals(principalDetails.getMember().getId())) {
            throw new BusinessException(ErrorCode.NOT_QNA_AUTHOR);
        }
    }
}