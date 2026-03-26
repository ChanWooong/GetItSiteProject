package com.getit.domain.lecture.controller;

import com.getit.domain.lecture.entity.LectureFile;
import com.getit.domain.lecture.service.LectureFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/lecture/files")
@RequiredArgsConstructor
public class LectureFileStreamController {

    private final LectureFileService lectureFileService;

    @GetMapping("/{fileId}/download")
    public ResponseEntity<Resource> download(@PathVariable Long fileId) {
        LectureFile lf = lectureFileService.getFileEntity(fileId);
        Path path = Path.of(lf.getFilePath());
        if (!Files.isRegularFile(path)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다.");
        }
        Resource resource = new FileSystemResource(path);
        String ct = lectureFileService.resolveContentType(lf.getFileName());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename(lf.getFileName(), StandardCharsets.UTF_8)
                        .build()
        );
        headers.setContentType(MediaType.parseMediaType(ct));
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    @GetMapping("/{fileId}/view")
    public ResponseEntity<Resource> view(@PathVariable Long fileId) {
        LectureFile lf = lectureFileService.getFileEntity(fileId);
        if (!lectureFileService.isPdf(lf)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "PDF만 미리보기할 수 있습니다.");
        }
        Path path = Path.of(lf.getFilePath());
        if (!Files.isRegularFile(path)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다.");
        }
        Resource resource = new FileSystemResource(path);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(
                ContentDisposition.inline()
                        .filename(lf.getFileName(), StandardCharsets.UTF_8)
                        .build()
        );
        headers.setContentType(MediaType.APPLICATION_PDF);
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}
