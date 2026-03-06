package com.getit.domain.admin.apply.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminApplicantInfoResponse {
    private final Long memberId;
    private final String name;
    private final String studentId;
    private final String college;
    private final String department;
    private final String cellNum;
    private final String email;
}