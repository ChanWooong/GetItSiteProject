package com.getit.domain.lecture.service;

import com.getit.domain.lecture.Track;
import com.getit.domain.lecture.dto.LectureResponse;
import com.getit.domain.lecture.entity.Lecture;
import com.getit.domain.lecture.entity.LectureRecord;
import com.getit.domain.lecture.repository.LectureRecordRepository;
import com.getit.domain.lecture.repository.LectureRepository;
import com.getit.domain.member.entity.Member;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureService {

    private final LectureRepository lectureRepository;
    private final LectureRecordRepository lectureRecordRepository;

    public List<LectureResponse> getLectures(Member member, String trackName) {
        Track track = Track.valueOf(trackName.toUpperCase());
        List<Lecture> lectures = lectureRepository.findByTrackOrderByWeekAsc(track);

        List<LectureResponse> response = new ArrayList<>();

        boolean isPrevDone = true;
        for (Lecture lecture : lectures) {
            LectureRecord record = lectureRecordRepository.findByMemberAndLecture(member, lecture)
                    .orElse(null);
            String status = "LOCKED";
            boolean isCurrentDone = (record != null && record.isVideoDone());
            if (isCurrentDone) {
                status = "DONE";
            } else if (isPrevDone) {
                status = "PROGRESS";
            }
            response.add(new LectureResponse(lecture, status));
            isPrevDone = isCurrentDone;
        }
        return response;
    }

    @Transactional
    public void completeVideo(Member member, Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("없는 강의"));

        LectureRecord record = lectureRecordRepository.findByMemberAndLecture(member, lecture)
                .orElseGet(() -> LectureRecord.builder()
                        .member(member)
                        .lecture(lecture)
                        .isVideoDone(false)
                        .isHwDone(false)
                        .build());

        record.completeVideo();
        lectureRecordRepository.save(record);
    }
}
