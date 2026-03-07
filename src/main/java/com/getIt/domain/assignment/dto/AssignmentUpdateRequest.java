package com.getit.domain.assignment.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AssignmentUpdateRequest {

    private String comment;

    private List<Long> deletedFiles;
}
