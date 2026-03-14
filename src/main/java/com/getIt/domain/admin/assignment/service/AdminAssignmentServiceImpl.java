package com.getit.domain.admin.assignment.service;

import com.getit.domain.admin.assignment.dto.mapper.AdminAssignmentMapper;
import com.getit.domain.admin.assignment.dto.response.AdminAssignmentListResponse;
import com.getit.domain.assignment.entity.Assignment;
import com.getit.domain.assignment.entity.AssignmentFile;
import com.getit.domain.assignment.repository.AssignmentFileRepository;
import com.getit.domain.assignment.repository.AssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminAssignmentServiceImpl implements AdminAssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final AssignmentFileRepository assignmentFileRepository;

    // 부원들이 제출한 전체 과제 조회 
    @Override
    public Page<AdminAssignmentListResponse> getAllAssignments(Pageable pageable) {

        // Assignment 페이징 조회
        Page<Assignment> assignmentPage = assignmentRepository.findAllWithTaskAndLecture(pageable);

        List<Assignment> assignments = assignmentPage.getContent();

        if (assignments.isEmpty()) {
            return Page.empty(pageable);
        }

        // assignmentId 목록 추출
        List<Long> assignmentIds = assignments.stream()
                .map(Assignment::getId)
                .toList();

        // AssignmentFile 조회
        List<AssignmentFile> files =
                assignmentFileRepository.findByAssignmentIdIn(assignmentIds);

        // assignmentId 기준 Map 생성
        Map<Long, List<AssignmentFile>> fileMap =
                files.stream()
                        .collect(Collectors.groupingBy(
                                file -> file.getAssignment().getId()
                        ));

        // Entity → DTO 변환
        List<AdminAssignmentListResponse> dtoList =
                AdminAssignmentMapper.toResponseList(assignments, fileMap);

        return new PageImpl<>(dtoList, pageable, assignmentPage.getTotalElements());
    }
}