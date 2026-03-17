package com.getit.domain.admin.assignment.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AssignmentFileInfoDto {

    private Long fileId;
    private String fileName;
    // ADMIN일지라도 클라이언트에 서버 내부 경로를 알려주는 것은 좋지 않을 것 같음.
    // private String filePath;

}
