package com.getit.domain.assignment.service;

import com.getit.domain.assignment.dto.FileDownloadDto;
import com.getit.domain.assignment.dto.AdminAssignmentDetailResponse;
import com.getit.domain.assignment.dto.AdminAssignmentListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminAssignmentService {

    // 부원들이 제출한 전체 과제 조회
    Page<AdminAssignmentListResponse> getAllAssignments(Pageable pageable);

    AdminAssignmentDetailResponse getAssignmentDetail(Long lectureId, Long memberId);

    FileDownloadDto downloadFile(Long fileId);
}