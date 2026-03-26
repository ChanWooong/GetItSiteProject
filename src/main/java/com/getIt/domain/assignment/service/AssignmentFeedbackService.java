package com.getit.domain.assignment.service;

import com.getit.domain.assignment.dto.AssignmentFeedbackCreateRequestDto;
import com.getit.domain.assignment.dto.AssignmentFeedbackResponseDto;
import com.getit.domain.assignment.dto.AssignmentFeedbackUpdateRequestDto;
import com.getit.domain.assignment.entity.Assignment;
import com.getit.domain.assignment.entity.AssignmentFeedback;
import com.getit.domain.assignment.repository.AssignmentFeedbackRepository;
import com.getit.domain.assignment.repository.AssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentFeedbackService {

    private final AssignmentRepository assignmentRepository;
    private final AssignmentFeedbackRepository assignmentFeedbackRepository;

    @Transactional(readOnly = true)
    public List<AssignmentFeedbackResponseDto> getFeedbacksForAdmin(Long assignmentId) {
        // assignment 존재 확인
        assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 과제를 찾을 수 없습니다."));

        return assignmentFeedbackRepository.findAllByAssignmentIdOrderByCreatedAtAsc(List.of(assignmentId))
                .stream()
                .map(AssignmentFeedbackResponseDto::from)
                .toList();
    }

    @Transactional
    public AssignmentFeedbackResponseDto createFeedback(Long assignmentId, AssignmentFeedbackCreateRequestDto request) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 과제를 찾을 수 없습니다."));

        AssignmentFeedback feedback = AssignmentFeedback.builder()
                .assignment(assignment)
                .content(request.getContent())
                .build();
        feedback = assignmentFeedbackRepository.save(feedback);
        return AssignmentFeedbackResponseDto.from(feedback);
    }

    @Transactional
    public AssignmentFeedbackResponseDto updateFeedback(Long feedbackId, AssignmentFeedbackUpdateRequestDto request) {
        AssignmentFeedback feedback = assignmentFeedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 코멘트를 찾을 수 없습니다."));
        feedback.updateContent(request.getContent());
        return AssignmentFeedbackResponseDto.from(feedback);
    }

    @Transactional
    public void deleteFeedback(Long feedbackId) {
        AssignmentFeedback feedback = assignmentFeedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 코멘트를 찾을 수 없습니다."));
        assignmentFeedbackRepository.delete(feedback);
    }
}

