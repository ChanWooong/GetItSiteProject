package com.getit.domain.admin.apply.service;

import com.getit.domain.admin.apply.dto.response.AdminApplyDetailResponse;
import com.getit.domain.admin.apply.dto.response.AdminApplyListResponse;
import com.getit.domain.admin.apply.dto.mapper.AdminApplyMapper;
import com.getit.domain.admin.apply.repository.AdminApplyRepository;
import com.getit.domain.admin.apply.entity.Application;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

//  관리자 전용 지원서 조회 서비스
//  - Repository 호출
//  - Draft 제외 정책 적용
//  - 존재 여부 검증
//  - Entity → DTO 변환
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminApplyServiceImpl implements AdminApplyService {

private final AdminApplyRepository adminApplyRepository;
private final AdminApplyMapper adminApplyMapper;

//      모든 지원서 조회
//      - isDraft = false 인 데이터만 조회
@Override
public List<AdminApplyListResponse> getAllApplies() {

        List<Application> applications =
                adminApplyRepository.findAllByIsDraftFalse();

        return applications.stream()
                .map(adminApplyMapper::toListResponse)
                .collect(Collectors.toList());
}

//      특정 지원서 상세 조회
//      - 존재하지 않으면 EntityNotFoundException 발생
@Override
public AdminApplyDetailResponse getApplyDetail(Long id) {

Application application = adminApplyRepository
        .findById(id)
        .filter(app -> !app.getIsDraft())
        .orElseThrow(() ->
                new EntityNotFoundException("해당 지원서를 찾을 수 없습니다.")
        );

        return adminApplyMapper.toDetailResponse(application);
}
}