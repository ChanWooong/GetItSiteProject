package com.getit.domain.lecture.repository;

import com.getit.domain.assignment.TrackType;
import com.getit.domain.lecture.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    Optional<Lecture> findByWeekAndType(Integer week, TrackType type);
}
