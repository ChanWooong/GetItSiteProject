package com.getit.domain.qna.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class QnaRoomResponse {
    private Long memberId;
    private String memberName;
    private String lastMessage;
    private String lastSender;
    private LocalDateTime lastMessageAt;
    private boolean unanswered;
}