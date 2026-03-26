package com.getit.domain.lecture.service;

import com.getit.domain.lecture.dto.AdminLectureMemberResponseDto;
import com.getit.domain.lecture.dto.LectureCreateRequestDto;
import com.getit.domain.lecture.dto.LectureUpdateRequestDto;
import com.getit.domain.assignment.entity.Task;
import com.getit.domain.assignment.repository.TaskRepository;
import com.getit.domain.lecture.entity.Lecture;
import com.getit.domain.lecture.entity.LectureFile;
import com.getit.domain.lecture.repository.LectureFileRepository;
import com.getit.domain.lecture.repository.LectureRepository;
import com.getit.domain.member.entity.Member;
import com.getit.domain.member.repository.MemberRepository;
import com.getit.global.exception.ErrorCode;
import com.getit.global.exception.GlobalExceptionManager.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminLectureService {

    private static final String DEFAULT_TASK_DESCRIPTION = "과제 안내";

    private final LectureRepository lectureRepository;
    private final MemberRepository memberRepository;
    private final TaskRepository taskRepository;
    private final LectureFileRepository lectureFileRepository;
    private final LectureFileStorageService lectureFileStorageService;

    public Lecture findById(Long id){
        return lectureRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.LECTURE_NOT_FOUND));
    }

    @Transactional
    public void createLecture(LectureCreateRequestDto request) {

        String videoUrl = normalizeUrl(request.getVideoUrl());
        String resourceUrl = normalizeUrl(request.getResourceUrl());

        Lecture lecture = Lecture.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .week(request.getWeek())
                .type(request.getType())
                .videoUrl(videoUrl)
                .resourceUrl(resourceUrl)
                .build();

        lectureRepository.save(lecture);

        Task task = Task.builder()
                .lecture(lecture)
                .title(request.getTitle())
                .description(DEFAULT_TASK_DESCRIPTION)
                .deadline(null)
                .build();
        taskRepository.save(task);
    }

    @Transactional
    public void updateLecture(Long id, LectureUpdateRequestDto request) {

        Lecture lecture = findById(id);

        lecture.update(
                request.getTitle(),
                request.getDescription(),
                request.getWeek(),
                request.getType(),
                normalizeUrl(request.getVideoUrl()),
                normalizeUrl(request.getResourceUrl()) 
        );
    }

    @Transactional
    public void deleteLecture(Long id) {
        Lecture lecture = findById(id);
        List<String> filePaths = lectureFileRepository.findAllByLecture_IdOrderByIdAsc(id).stream()
            .map(LectureFile::getFilePath)
            .toList();  
        lectureRepository.delete(lecture);
        for(String path : filePaths) {
            try{
                lectureFileStorageService.deleteStoredFile(path);
            } catch (Exception e) {
                log.error("강의 자료 파일 삭제 실패: {}", path, e);
            }
        }
    }

    @Transactional(readOnly = true)
    public AdminLectureMemberResponseDto getLectureWithMemberInfo(Long lectureId, Long memberId) {

        Lecture lecture = findById(lectureId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        return AdminLectureMemberResponseDto.of(lecture, member);
    }

    public String normalizeUrl(String newUrl) {
        if (newUrl == null) {
            return null; // 필드가 전달되지 않았으므로 무시
        }
        return newUrl.isBlank()? "": newUrl;
        
    }
}
