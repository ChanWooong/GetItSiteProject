package com.getit.domain.lecture.repository;

import com.getit.domain.lecture.entity.Lecture;
import com.getit.domain.lecture.entity.LectureRecord;
import com.getit.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LectureRecordRepository extends JpaRepository<LectureRecord, Long> {

    Optional<LectureRecord> findByMemberAndLecture(Member member, Lecture lecture);

    // 특정 유저의 모든 기록 가져오기 (나중에 진도율 계산할 때 씀)
    List<LectureRecord> findByMember(Member member);
}