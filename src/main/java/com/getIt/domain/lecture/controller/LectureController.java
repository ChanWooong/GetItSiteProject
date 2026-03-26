package com.getit.domain.lecture.controller;

import com.getit.domain.lecture.dto.LectureDetailResponseDto;
import com.getit.domain.lecture.dto.LectureFileResponseDto;
import com.getit.domain.lecture.dto.LectureResponseDto;
import com.getit.domain.lecture.service.LectureFileService;
import com.getit.domain.lecture.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;
    private final LectureFileService lectureFileService;

    @GetMapping("/lectures")
    public ResponseEntity<Page<LectureResponseDto>> getLectures(Pageable pageable) {

        return ResponseEntity.ok(lectureService.getLectures(pageable));
    }

    @GetMapping("/lecture/{id}")
    public ResponseEntity<LectureDetailResponseDto> getLectureDetail(@PathVariable Long id) {

        return ResponseEntity.ok(lectureService.getLectureDetail(id));
    }

    @GetMapping("/lecture/{lectureId}/files")
    public ResponseEntity<List<LectureFileResponseDto>> getLectureFiles(@PathVariable Long lectureId) {
        return ResponseEntity.ok(lectureFileService.listFiles(lectureId));
    }
}