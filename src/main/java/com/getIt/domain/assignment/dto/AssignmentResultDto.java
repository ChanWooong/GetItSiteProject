package com.getit.domain.assignment.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class AssignmentResultDto {
    protected List<String> successFiles;
    protected List<String> failedFiles;
}
