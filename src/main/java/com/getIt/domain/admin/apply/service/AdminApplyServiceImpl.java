package com.getit.domain.admin.apply.service;

import com.getit.domain.admin.apply.dto.response.AdminApplicantInfoResponse;
import com.getit.domain.admin.apply.dto.response.AdminApplyDetailResponse;
import com.getit.domain.admin.apply.dto.response.AdminApplyListResponse;
import com.getit.domain.admin.apply.dto.mapper.AdminApplyMapper;
import com.getit.domain.admin.apply.repository.AdminApplyRepository;
import com.getit.domain.apply.entity.Application;
import com.getit.domain.member.entity.MemberInfo;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

// 관리자 전용 지원서 조회 서비스
// - Repository 호출
// - Draft 제외 정책 적용
// - 존재 여부 검증
// - Entity → DTO 변환
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminApplyServiceImpl implements AdminApplyService {

    private final AdminApplyRepository adminApplyRepository;
    private final AdminApplyMapper adminApplyMapper;

    // 모든 지원서 조회 
    // - isDraft = false 인 데이터만 조회
// AdminApplyServiceImpl.java

    @Override
    public List<AdminApplyListResponse> getAllApplies(Pageable pageable) {
        // 1. DB에서 페이징 처리된 엔티티를 가져옵니다.
        Page<Application> applicationPage =
                adminApplyRepository.findAllByIsDraftFalseOrderByIdDesc(pageable);

        // 2. PageImpl로 감싸지 말고, List 그 자체만 변환해서 반환합니다!
        return applicationPage.getContent()
                .stream()
                .map(adminApplyMapper::toListResponse)
                .collect(Collectors.toList());
    }

    // 특정 지원서 상세 조회
    // - 존재하지 않으면 EntityNotFoundException 발생
    @Override
    public AdminApplyDetailResponse getApplyDetail(Long id) {

        Application application = adminApplyRepository
                .findByIdWithMember(id)
                .filter(app -> !app.getIsDraft())
                .orElseThrow(() ->
                        new EntityNotFoundException("해당 지원서를 찾을 수 없습니다.")
                );

        return adminApplyMapper.toDetailResponse(application);
    }

    @Override
    public AdminApplicantInfoResponse getApplicantInfo(Long applicationId) {
        Application application = adminApplyRepository.findByIdWithMember(applicationId)
                .orElseThrow(() -> new EntityNotFoundException("해당 지원서를 찾을 수 없습니다."));

        MemberInfo info = application.getMember().getMemberInfo();

        return AdminApplicantInfoResponse.builder()
                .memberId(application.getMember().getId())
                .name(info != null ? info.getName() : null)
                .studentId(info != null ? info.getStudentId() : null)
                .college(info != null ? info.getCollege().name() : null)
                .department(info != null ? info.getDepartment() : null)
                .cellNum(info != null ? info.getCellNum() : null)
                .email(application.getMember().getEmail())
                .build();
    }
}