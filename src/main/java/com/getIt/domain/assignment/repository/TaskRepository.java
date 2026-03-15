package com.getit.domain.assignment.repository;

import com.getit.domain.assignment.TrackType;
import com.getit.domain.assignment.entity.Task;
import com.getit.domain.lecture.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    // 미사용
    // Optional<Task> findByTypeAndWeek(TrackType type, Integer week);

    Optional<Task> findByLecture(Lecture lecture);

    //  특정 주차 과제 조회
    // List<Task> findByWeek(Integer week);

    //  과제 타입으로 조회 (SW / STARTUP)
    // List<Task> findByType(TrackType type);

}
