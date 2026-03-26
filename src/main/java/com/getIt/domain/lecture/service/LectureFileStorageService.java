package com.getit.domain.lecture.service;

import com.getit.domain.assignment.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;
import java.util.Objects;
import java.util.Set;

/**
 * 강의 자료 파일 저장 경로: {@code ${file.upload.dir}/lectures/{lectureId}/}
 * 파일명: {@code {uuid}_{원본안전파일명}}
 */
@Service
@RequiredArgsConstructor
public class LectureFileStorageService {

    private final FileStorageService fileStorageService;

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            "pdf", "ppt", "pptx", "doc", "docx", "xls", "xlsx",
            "zip", "hwp", "txt", "png", "jpg", "jpeg", "gif", "webp"
    );

    public String storeLectureFile(MultipartFile file, Long lectureId) {
        String original = Objects.requireNonNull(file.getOriginalFilename(), "파일명이 없습니다.");
        String safeName = validateAndGetSafeFileName(original);
        String ext = extensionOf(safeName);
        if (ext.isEmpty() || !ALLOWED_EXTENSIONS.contains(ext.toLowerCase(Locale.ROOT))) {
            throw new IllegalArgumentException("허용되지 않는 파일 형식입니다.");
        }
        String dirName = "lectures/" + lectureId;
        fileStorageService.createAssignmentDir(dirName);
        return fileStorageService.storeFile(file, dirName);
    }

    public void deleteStoredFile(String absoluteFilePath) {
        fileStorageService.deleteFile(absoluteFilePath);
    }

    private static String validateAndGetSafeFileName(String originalFileName) {
        String safeName = java.nio.file.Path.of(originalFileName).getFileName().toString();
        if (safeName.isBlank()) {
            throw new IllegalArgumentException("유효하지 않은 파일명입니다.");
        }
        return safeName;
    }

    private static String extensionOf(String fileName) {
        int dot = fileName.lastIndexOf('.');
        if (dot < 0 || dot == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(dot + 1);
    }
}
