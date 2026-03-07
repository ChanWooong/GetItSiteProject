package com.getit.domain.assignment.repository;

import com.getit.domain.assignment.entity.AssignmentFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentFileRepository extends JpaRepository<AssignmentFile, Long> {
}
