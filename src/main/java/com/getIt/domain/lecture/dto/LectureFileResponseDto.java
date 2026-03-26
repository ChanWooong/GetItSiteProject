package com.getit.domain.lecture.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LectureFileResponseDto {

    private Long fileId;
    private String fileName;
    private String contentType;
    private boolean pdf;
    private String downloadUrl;
    /** PDF일 때만 인라인 보기 URL, 그 외 null */
    private String viewUrl;
}
