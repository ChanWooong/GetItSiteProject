package com.getit.domain.admin.lecture.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskUpdateRequestDto {

    private String title;

    private String description;

    private LocalDateTime deadline;
}
