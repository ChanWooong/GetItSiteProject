package com.getit.domain.lecture.service;

import com.getit.domain.lecture.dto.LectureDetailResponseDto;
import com.getit.domain.lecture.dto.LectureResponseDto;
import com.getit.domain.lecture.entity.Lecture;
import com.getit.domain.lecture.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureService {

    private final LectureRepository lectureRepository;

    public Page<LectureResponseDto> getLectures(Pageable pageable) {

        return lectureRepository.findAll(pageable)
                .map(LectureResponseDto::from);
    }

    public LectureDetailResponseDto getLectureDetail(Long id) {

        Lecture lecture = lectureRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Lecture not found"));

        return LectureDetailResponseDto.from(lecture);
    }
}