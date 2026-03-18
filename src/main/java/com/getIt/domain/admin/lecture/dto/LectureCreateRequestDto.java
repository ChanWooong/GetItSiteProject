package com.getit.domain.admin.lecture.dto;

import com.getit.domain.assignment.TrackType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LectureCreateRequestDto {

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotBlank(message = "설명은 필수입니다.")
    private String description;

    @NotNull(message = "주차는 필수입니다.")
    @Min(value = 1, message = "주차는 1 이상이어야 합니다.")
    private Integer week;

    @NotNull(message = "트랙 타입은 필수입니다.")
    private TrackType type;

    @URL(message = "올바른 URL 형식이어야 합니다.")
    private String videoUrl;

    @URL(message = "올바른 URL 형식이어야 합니다.")
    private String resourceUrl;
}