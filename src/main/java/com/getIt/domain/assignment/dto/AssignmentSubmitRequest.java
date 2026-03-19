package com.getit.domain.assignment.dto;

import com.getit.domain.assignment.TrackType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AssignmentSubmitRequest {

    @NotNull
    @Positive
    private Integer week;

    @NotNull
    private TrackType type;

    private String comment;

    /** GitHub 저장소/링크 (선택) */
    private String githubUrl;
}
