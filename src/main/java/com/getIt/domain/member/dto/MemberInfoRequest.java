package com.getit.domain.member.dto;

import com.getit.domain.member.College;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInfoRequest {

    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    private String name;

    @NotNull(message = "학번은 필수 입력 항목입니다.")
    @Pattern(regexp = "^[0-9]{10}$", message="학번은 숫자 10자리여야 합니다.")
    private String studentId;

    @NotNull(message = "단과대학은 필수 항목입니다.")
    private College college;

    @NotBlank(message = "학과(부)는 필수 입력 항목입니다.")
    private String department;

    @NotBlank(message = "전화번호는 필수 입력 항목입니다.")
    @Pattern(regexp = "^010-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다. (010-XXXX-XXXX)")
    private String cellNum;

    // 추가로 필요한 정보가 있다면 여기에 더 넣으시면 됩니다 (예: 트랙, 학년 등)
}