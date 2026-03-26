package com.getit.domain.lecture.service;

import com.getit.domain.lecture.dto.LectureFileResponseDto;
import com.getit.domain.lecture.entity.Lecture;
import com.getit.domain.lecture.entity.LectureFile;
import com.getit.domain.lecture.repository.LectureFileRepository;
import com.getit.domain.lecture.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LectureFileService {

    private final LectureRepository lectureRepository;
    private final LectureFileRepository lectureFileRepository;
    private final LectureFileStorageService lectureFileStorageService;

    @Transactional(readOnly = true)
    public List<LectureFileResponseDto> listFiles(Long lectureId) {
        ensureLectureExists(lectureId);
        return lectureFileRepository.findAllByLecture_IdOrderByIdAsc(lectureId).stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public List<LectureFileResponseDto> uploadFiles(Long lectureId, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "업로드할 파일이 없습니다.");
        }
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "강의를 찾을 수 없습니다."));
        List<LectureFileResponseDto> out = new ArrayList<>();
        List<String> storedPaths = new ArrayList<>();
        try{
            for(MultipartFile mf : files) {
                if (mf == null || mf.isEmpty()) {
                    continue;
                }
                String path = lectureFileStorageService.storeLectureFile(mf, lectureId);
                storedPaths.add(path);
                String displayName = safeOriginalName(mf.getOriginalFilename());
                LectureFile row = LectureFile.builder()
                        .lecture(lecture)
                        .fileName(displayName)
                        .filePath(path)
                        .build();
                lectureFileRepository.save(row);
                out.add(toDto(row));
            }
        } catch (Exception e) {
            storedPaths.forEach(path ->{
                try{
                    lectureFileStorageService.deleteStoredFile(path);
                }catch (Exception ex){
                    log.error("강의 자료 파일 삭제 실패: {}", path, ex);
                }
            });
        }
        if(out.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효한 파일이 없습니다.");
        }
        return out;
    }
    @Transactional
    public void deleteFile(Long lectureId, Long fileId) {
        LectureFile lf = lectureFileRepository.findByIdAndLecture_Id(fileId, lectureId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."));
        lectureFileStorageService.deleteStoredFile(lf.getFilePath());
        lectureFileRepository.delete(lf);
    }

    @Transactional(readOnly = true)
    public LectureFile getFileEntity(Long fileId) {
        return lectureFileRepository.findById(fileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."));
    }

    public boolean isPdf(LectureFile file) {
        return file.getFileName().toLowerCase(Locale.ROOT).endsWith(".pdf");
    }

    public String resolveContentType(String fileName) {
        String ext = extensionOf(fileName).toLowerCase(Locale.ROOT);
        return switch (ext) {
            case "pdf" -> "application/pdf";
            case "png" -> "image/png";
            case "jpg", "jpeg" -> "image/jpeg";
            case "gif" -> "image/gif";
            case "webp" -> "image/webp";
            case "zip" -> "application/zip";
            case "txt" -> "text/plain; charset=UTF-8";
            default -> "application/octet-stream";
        };
    }

    private void ensureLectureExists(Long lectureId) {
        if (!lectureRepository.existsById(lectureId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "강의를 찾을 수 없습니다.");
        }
    }

    private LectureFileResponseDto toDto(LectureFile f) {
        String name = f.getFileName();
        boolean pdf = name.toLowerCase(Locale.ROOT).endsWith(".pdf");
        return LectureFileResponseDto.builder()
                .fileId(f.getId())
                .fileName(name)
                .contentType(resolveContentType(name))
                .pdf(pdf)
                .downloadUrl("/api/lecture/files/" + f.getId() + "/download")
                .viewUrl(pdf ? "/api/lecture/files/" + f.getId() + "/view" : null)
                .build();
    }

    private static String safeOriginalName(String original) {
        String s = Path.of(Objects.requireNonNull(original, "파일명이 없습니다.")).getFileName().toString();
        if (s.isBlank()) {
            throw new IllegalArgumentException("유효하지 않은 파일명입니다.");
        }
        return s;
    }

    private static String extensionOf(String fileName) {
        int dot = fileName.lastIndexOf('.');
        if (dot < 0 || dot == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(dot + 1);
    }
}
