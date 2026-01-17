package com.getit.domain.lecture.repository;

import com.getit.domain.lecture.Track;
import com.getit.domain.lecture.entity.Lecture;
import com.getit.domain.lecture.entity.LectureRecord;
import com.getit.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    List<Lecture> findByTrackOrderByWeekAsc(Track track);
}
