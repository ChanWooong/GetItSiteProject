package com.getit.domain.lecture.controller;

import com.getit.domain.lecture.dto.AdminLectureMemberResponseDto;
import com.getit.domain.lecture.dto.LectureCreateRequestDto;
import com.getit.domain.lecture.dto.LectureFileResponseDto;
import com.getit.domain.lecture.dto.LectureUpdateRequestDto;
import com.getit.domain.lecture.dto.TaskCreateRequestDto;
import com.getit.domain.lecture.dto.TaskResponseDto;
import com.getit.domain.lecture.dto.TaskUpdateRequestDto;
import com.getit.domain.lecture.service.AdminLectureService;
import com.getit.domain.lecture.service.AdminTaskService;
import com.getit.domain.lecture.service.LectureFileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/lecture")
@RequiredArgsConstructor
public class AdminLectureController {

    private final AdminLectureService adminLectureService;
    private final AdminTaskService adminTaskService;
    private final LectureFileService lectureFileService;

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

    @GetMapping("/{lectureId}/task")
    public ResponseEntity<TaskResponseDto> getTask(@PathVariable Long lectureId) {
        return ResponseEntity.ok(adminTaskService.getTask(lectureId));
    }

    @PostMapping("/{lectureId}/task")
    public ResponseEntity<TaskResponseDto> createTask(
            @PathVariable Long lectureId,
            @Valid @RequestBody TaskCreateRequestDto request) {
        TaskResponseDto created = adminTaskService.createTask(lectureId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{lectureId}/task")
    public ResponseEntity<TaskResponseDto> updateTask(
            @PathVariable Long lectureId,
            @Valid @RequestBody TaskUpdateRequestDto request) {
        return ResponseEntity.ok(adminTaskService.updateTask(lectureId, request));
    }

    @DeleteMapping("/{lectureId}/task")
    public ResponseEntity<Void> deleteTask(@PathVariable Long lectureId) {
        adminTaskService.deleteTask(lectureId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{lectureId}/files")
    public ResponseEntity<List<LectureFileResponseDto>> listLectureFiles(@PathVariable Long lectureId) {
        return ResponseEntity.ok(lectureFileService.listFiles(lectureId));
    }

    @PostMapping(value = "/{lectureId}/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<LectureFileResponseDto>> uploadLectureFiles(
            @PathVariable Long lectureId,
            @RequestPart("files") List<MultipartFile> files) {
        List<LectureFileResponseDto> created = lectureFileService.uploadFiles(lectureId, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{lectureId}/files/{fileId}")
    public ResponseEntity<Void> deleteLectureFile(
            @PathVariable Long lectureId,
            @PathVariable Long fileId) {
        lectureFileService.deleteFile(lectureId, fileId);
        return ResponseEntity.noContent().build();
    }
}