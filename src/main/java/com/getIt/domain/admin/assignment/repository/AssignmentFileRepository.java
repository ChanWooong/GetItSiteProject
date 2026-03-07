package com.getit.domain.admin.assignment.repository;

import com.getit.domain.admin.assignment.entity.AssignmentFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentFileRepository extends JpaRepository<AssignmentFile, Long> {

    //  특정 Assignment에 속한 파일 조회
    List<AssignmentFile> findByAssignmentId(Long assignmentId);


    //  여러 Assignment의 파일을 한번에 조회
    List<AssignmentFile> findByAssignmentIdIn(List<Long> assignmentIds);

}