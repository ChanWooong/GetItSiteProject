package com.getit.domain.admin.lecture.controller;

import com.getit.domain.admin.lecture.dto.AdminLectureMemberResponseDto;
import com.getit.domain.admin.lecture.dto.LectureCreateRequestDto;
import com.getit.domain.admin.lecture.dto.LectureUpdateRequestDto;
import com.getit.domain.admin.lecture.service.AdminLectureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/lecture")
@RequiredArgsConstructor
public class AdminLectureController {

    private final AdminLectureService adminLectureService;

    @PostMapping
    public ResponseEntity<Void> createLecture(@Valid @RequestBody LectureCreateRequestDto request) {
        adminLectureService.createLecture(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateLecture(
            @PathVariable Long id,
            @Valid @RequestBody LectureUpdateRequestDto request) {
        adminLectureService.updateLecture(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLecture(@PathVariable Long id) {
        adminLectureService.deleteLecture(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/{memberId}")
    public ResponseEntity<AdminLectureMemberResponseDto> getLectureWithMemberInfo(
            @PathVariable Long id,
            @PathVariable Long memberId) {
        return ResponseEntity.ok(adminLectureService.getLectureWithMemberInfo(id, memberId));
    }
}