package com.getit.domain.admin.lecture.service;

import com.getit.domain.admin.lecture.dto.AdminLectureMemberResponseDto;
import com.getit.domain.admin.lecture.dto.LectureCreateRequestDto;
import com.getit.domain.admin.lecture.dto.LectureUpdateRequestDto;
import com.getit.domain.lecture.entity.Lecture;
import com.getit.domain.lecture.repository.LectureRepository;
import com.getit.domain.member.entity.Member;
import com.getit.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AdminLectureService {

    private final LectureRepository lectureRepository;
    private final MemberRepository memberRepository;

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
    }

    @Transactional
    public void updateLecture(Long id, LectureUpdateRequestDto request) {

        Lecture lecture = lectureRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Lecture not found"));

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

        Lecture lecture = lectureRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Lecture not found"));

        lectureRepository.delete(lecture);
    }

    @Transactional(readOnly = true)
    public AdminLectureMemberResponseDto getLectureWithMemberInfo(Long lectureId, Long memberId) {

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Lecture not found"));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));

        return AdminLectureMemberResponseDto.of(lecture, member);
    }

    public void normalizeUrl(String newUrl) {
    if (newUrl == null) {
        return; // 필드가 전달되지 않았으므로 무시
    }
    
    if (newUrl.isEmpty()) {
        this.videoUrl = null; // 빈 문자열이 오면 명시적으로 삭제(null 처리)
    } else {
        this.videoUrl = newUrl; // 새 값 반영
    }
}
}
