package com.getit.domain.assignment.repository;

import com.getit.domain.assignment.entity.AssignmentFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentFeedbackRepository extends JpaRepository<AssignmentFeedback, Long> {

    List<AssignmentFeedback> findAllByAssignmentIdInOrderByCreatedAtAsc(List<Long> assignmentId);
}

