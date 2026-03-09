package com.getit.domain.assignment.dto;

import com.getit.domain.assignment.Status;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class AssignmentUpdateResultDto extends AssignmentResultDto {
    private Long assignmentId;
    private Status status;
    private String updatedAt;
}
