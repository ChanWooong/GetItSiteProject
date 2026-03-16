package com.getit.domain.admin.lecture.dto;

import com.getit.domain.assignment.TrackType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LectureUpdateRequestDto {

    @Pattern(regexp = ".*\\S.*", message = "제목은 공백만 입력할 수 없습니다.")
    private String title;

    @Pattern(regexp = ".*\\S.*", message = "설명은 공백만 입력할 수 없습니다.")
    private String description;

    @Min(value = 1, message = "주차는 1 이상이어야 합니다.")
    private Integer week;

    private TrackType type;

    @URL(message = "올바른 URL 형식이어야 합니다.")
    private String videoUrl;
}