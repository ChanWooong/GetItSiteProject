package com.getit.domain.lecture.dto;

import com.getit.domain.assignment.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDto {

    private Long id;
    private Long lectureId;
    private String title;
    private String description;
    private String deadline;
    private String createdAt;

    public static TaskResponseDto from(Task task) {
        return TaskResponseDto.builder()
                .id(task.getId())
                .lectureId(task.getLecture().getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .deadline(task.getDeadline() != null ? task.getDeadline().toString() : null)
                .createdAt(task.getCreatedAt() != null ? task.getCreatedAt().toString() : null)
                .build();
    }
}
