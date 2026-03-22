package com.getit.domain.assignment.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AssignmentUpdateRequest {

    private String comment;

    /** GitHub 저장소/링크 (선택, null이면 변경 없음) */
    private String githubUrl;

    private List<Long> deletedFiles;
}
