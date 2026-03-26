package com.getit.domain.lecture.repository;

import com.getit.domain.lecture.entity.LectureFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LectureFileRepository extends JpaRepository<LectureFile, Long> {

    List<LectureFile> findAllByLecture_IdOrderByIdAsc(Long lectureId);

    Optional<LectureFile> findByIdAndLecture_Id(Long id, Long lectureId);
}
