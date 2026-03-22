package com.getit.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // ===== [C] 공통 (Common) =====
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "입력 값이 잘못되었습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C002", "지원하지 않는 HTTP 메서드입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C003", "서버 내부 오류가 발생했습니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "C004", "요청 파라미터의 타입이 잘못되었습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "C005", "접근 권한이 없습니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "C006", "요청한 리소스를 찾을 수 없습니다."),

    // ===== 인증/보안 (Security) =====
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "S001", "인증되지 않은 사용자입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "S002", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "S003", "만료된 토큰입니다."),
    OAUTH2_PROCESS_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S004", "소셜 로그인 처리 중 오류가 발생했습니다."),
    NEED_ADDITIONAL_INFO(HttpStatus.FORBIDDEN, "S005", "인증 후 추가 정보 입력이 필요합니다."),

    // ===== 멤버 관리 (Member Management) =====
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "존재하지 않는 회원입니다."),
    ALREADY_APPROVED_MEMBER(HttpStatus.BAD_REQUEST, "M002", "이미 승인된 회원입니다."),
    GUEST_CANNOT_ACCESS(HttpStatus.FORBIDDEN, "M003", "Guest 권한으로는 접근할 수 없습니다."),
    CANNOT_DELETE_ADMIN(HttpStatus.BAD_REQUEST, "M004", "최고 관리자는 삭제할 수 없습니다."),
    INVALID_ROLE_UPDATE(HttpStatus.BAD_REQUEST, "M005", "유효하지 않은 권한 변경 요청입니다."),

    // ===== 지원서 (Applies) =====
    APPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "AP001", "작성된 지원서(임시저장 포함)를 찾을 수 없습니다."),
    NOT_RECRUITMENT_PERIOD(HttpStatus.BAD_REQUEST, "AP002", "현재 모집 기간이 아닙니다."),
    ALREADY_SUBMITTED_APPLY(HttpStatus.CONFLICT, "AP003", "이미 최종 제출한 지원서가 존재합니다."),
    INVALID_RECRUITMENT_STATUS(HttpStatus.BAD_REQUEST, "AP004", "모집 상태(시작/종료) 설정이 잘못되었습니다."),

    // ===== 강좌 (Lecture) =====
    LECTURE_NOT_FOUND(HttpStatus.NOT_FOUND, "L001", "존재하지 않는 강좌입니다."),
    INVALID_LECTURE_TIME(HttpStatus.BAD_REQUEST, "L002", "강좌 시작 및 종료 시간이 잘못 설정되었습니다."),

    // ===== 과제 (Assignments) =====
    ASSIGNMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "A001", "존재하지 않는 과제입니다."),
    DUPLICATE_ASSIGNMENT_SUBMISSION(HttpStatus.CONFLICT, "A002", "해당 주차/타입에 이미 제출된 과제가 있습니다."),
    ASSIGNMENT_DEADLINE_EXPIRED(HttpStatus.BAD_REQUEST, "A003", "과제 제출 마감일이 지났습니다."),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "A004", "파일 업로드에 실패했습니다."),
    FILE_DOWNLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "A005", "파일 다운로드에 실패했습니다."),
    MAX_UPLOAD_SIZE_EXCEEDED(HttpStatus.PAYLOAD_TOO_LARGE, "A006", "업로드 가능한 최대 파일 용량을 초과했습니다."),
    INVALID_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "A007", "지원하지 않는 파일 형식입니다."),
    NOT_ASSIGNMENT_AUTHOR(HttpStatus.FORBIDDEN, "A008", "본인의 과제만 수정/조회할 수 있습니다."),
    TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "A009", "해당 과제 정보를 찾을 수 없습니다."),
    NOT_ASSIGNMENT_OWNER(HttpStatus.FORBIDDEN, "A010", "본인이 제출한 과제만 수정할 수 있습니다."),
    ALREADY_SUBMITTED_ASSIGNMENT(HttpStatus.BAD_REQUEST, "A011", "이미 해당 주차에 과제를 제출했습니다."),
    FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "A012", "파일 업로드 중 오류가 발생했습니다."),

    // ===== 질의응답 (Q&A) =====
    QNA_NOT_FOUND(HttpStatus.NOT_FOUND, "Q001", "해당 질문/답변을 찾을 수 없습니다."),
    NOT_QNA_AUTHOR(HttpStatus.FORBIDDEN, "Q002", "작성자 본인만 삭제할 수 있습니다."),
    ALREADY_ANSWERED(HttpStatus.CONFLICT, "Q003", "이미 답변이 등록된 질문입니다."),
    ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "Q004", "질문에 대한 답변이 존재하지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}