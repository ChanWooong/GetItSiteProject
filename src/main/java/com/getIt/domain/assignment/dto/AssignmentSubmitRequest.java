package com.getit.domain.assignment.dto;

import com.getit.domain.assignment.TaskType;
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
    private TaskType type;

    private String comment;
}
