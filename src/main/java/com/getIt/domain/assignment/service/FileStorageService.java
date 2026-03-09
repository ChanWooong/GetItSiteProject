package com.getit.domain.assignment.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class FileStorageService {

    private final String storagePath;

    public FileStorageService(@Value("${file.upload.dir}") String storagePath) {
        this.storagePath = storagePath;
        ensureDirExists();
    }

    private void ensureDirExists() {
        try {
            Path path = Path.of(storagePath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            throw new IllegalStateException("파일 업로드 디렉토리를 생성할 수 없습니다.", e); // 런타임 예외
        }
    }

    public void createAssignmentDir(String dirName) {
        Path dirPath = getValidatedDirPath(dirName);
        try {
            Files.createDirectories(dirPath);
        } catch (IOException e) {
            throw new IllegalStateException("과제 제출 디렉토리를 생성할 수 없습니다.", e);
        }
    }

    public String storeFile(MultipartFile file, String dirName) {
        Path baseDir = getValidatedDirPath(dirName);
        String originalFileName = Objects.requireNonNull(file.getOriginalFilename(), "파일명이 없습니다.");
        String safeName = Path.of(originalFileName).getFileName().toString();

        if (safeName.isBlank()) {
            throw new IllegalArgumentException("유효하지 않은 파일명입니다.");
        }

        String storedName = UUID.randomUUID() + "_" + safeName;
        Path filePath = baseDir.resolve(storedName).normalize();

        if (!filePath.startsWith(baseDir)) {
            throw new IllegalArgumentException("허용되지 않는 파일 경로입니다.");
        }

        try {
            file.transferTo(filePath.toFile());
            return filePath.toString();
        } catch (IOException e) {
            throw new IllegalStateException("파일 저장 중 문제가 발생했습니다.", e);
        }
    }

    public void deleteFile(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            return;
        }
        try {
            Path path = getValidatedFilePath(filePath);
            boolean deleted = Files.deleteIfExists(path);
            if (deleted) {
                log.info("물리적 파일 삭제 완료: {}", filePath);
            } else {
                log.warn("삭제하려는 물리적 파일이 존재하지 않습니다: {}", filePath);
            }
        } catch (IOException e) {
            throw new IllegalStateException("물리적 파일 삭제 중 문제가 발생했습니다: [" + filePath + "]", e);
        }
    }

    // 저장 중 오류 발생 시, 해당 과정에서 만들어진 dir, file 삭제 (롤백)
    public void deleteDir(String dirName) {
        Path dirPath = getValidatedDirPath(dirName);
        try {
            FileSystemUtils.deleteRecursively(dirPath);
        } catch (IOException e) {
            log.error("롤백 실패: {}", dirPath, e);
        }
    }

    private Path getValidatedDirPath(String dirName) {
        Path root = Path.of(storagePath).toAbsolutePath().normalize();
        Path dirPath = root.resolve(dirName).normalize();
        if (!dirPath.startsWith(root)) {
            throw new SecurityException("허용되지 않은 디렉토리 경로 조작 시도가 감지되었습니다: " + dirName);
        }
        return dirPath;
    }

    private Path getValidatedFilePath(String filePath) {
        Path root = Path.of(storagePath).toAbsolutePath().normalize();
        Path path = Path.of(filePath).toAbsolutePath().normalize();
        if (!path.startsWith(root)) {
            throw new SecurityException("허용되지 않은 물리적 파일 경로 조작 시도가 감지되었습니다: " + filePath);
        }
        return path;
    }
}
