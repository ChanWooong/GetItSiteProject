package com.getit.domain.admin.assignment.repository;

import com.getit.domain.admin.assignment.entity.Task;
import com.getit.domain.admin.assignment.entity.TaskType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    //  특정 주차 과제 조회
    List<Task> findByWeek(Integer week);

    //  과제 타입으로 조회 (SW / STARTUP)
    List<Task> findByType(TaskType type);

}