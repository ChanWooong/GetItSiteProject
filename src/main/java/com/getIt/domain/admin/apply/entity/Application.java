package com.getit.domain.admin.apply.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

//  지원서 엔티티
//  DB Table: application
@Entity
@Table(name = "application")
@Getter
@NoArgsConstructor
public class Application {

    //  지원서 PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //  지원자(Member) ID
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    //  질문 답변들
    //  answer1 ~ answer5 예시 (필요 시 확장 또는 현재 DB를 지원서와 각 질문에 대한 개별 답변으로 분리해야 됨)
    @Column(columnDefinition = "TEXT")
    private String answer1;

    @Column(columnDefinition = "TEXT")
    private String answer2;

    @Column(columnDefinition = "TEXT")
    private String answer3;

    @Column(columnDefinition = "TEXT")
    private String answer4;

    @Column(columnDefinition = "TEXT")
    private String answer5;

    //  임시 저장 여부
    @Column(name = "is_draft", nullable = false)
    private Boolean isDraft;

    //  answer1~N을 List 형태로 반환하는 편의 메서드
    public List<String> getAnswerList() {
        List<String> answers = new ArrayList<>();

        answers.add(answer1);
        answers.add(answer2);
        answers.add(answer3);
        answers.add(answer4);
        answers.add(answer5);

        return answers;
    }
}