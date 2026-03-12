package com.getit.domain.assignment.repository;

import com.getit.domain.assignment.TaskType;
import com.getit.domain.assignment.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByTypeAndWeek(TaskType type, Integer week);

    //  특정 주차 과제 조회
    List<Task> findByWeek(Integer week);

    //  과제 타입으로 조회 (SW / STARTUP)
    List<Task> findByType(TaskType type);

}
