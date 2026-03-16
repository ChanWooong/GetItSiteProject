package com.getit.domain.qna.dto;

import com.getit.domain.qna.entity.Qna;
import com.getit.domain.qna.entity.QnaAnswer;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@Builder
public class QnaChatMessage {
    private Long id;
    private String content;
    private String sender;          // "USER" or "ADMIN"
    private LocalDateTime createdAt;

    public static QnaChatMessage ofQuestion(Qna qna) {
        return QnaChatMessage.builder()
                .id(qna.getId())
                .content(qna.getContent())
                .sender("USER")
                .createdAt(qna.getCreatedAt())
                .build();
    }

    public static QnaChatMessage ofAnswer(QnaAnswer answer) {
        return QnaChatMessage.builder()
                .id(answer.getId())
                .content(answer.getContent())
                .sender("ADMIN")
                .createdAt(answer.getCreatedAt())
                .build();
    }

    // 질문 + 답변 합쳐서 시간순 정렬
    public static List<QnaChatMessage> merge(List<Qna> qnaList, List<QnaAnswer> answers) {
        List<QnaChatMessage> messages = new ArrayList<>();
        qnaList.forEach(q -> messages.add(ofQuestion(q)));
        answers.forEach(a -> messages.add(ofAnswer(a)));
        messages.sort(Comparator.comparing(QnaChatMessage::getCreatedAt));
        return messages;
    }
}