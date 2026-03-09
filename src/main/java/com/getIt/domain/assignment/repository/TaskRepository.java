package com.getit.domain.assignment.repository;

import com.getit.domain.assignment.TaskType;
import com.getit.domain.assignment.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByTypeAndWeek(TaskType type, Integer week);
}
