package com.getit.domain.lecture.controller;

import com.getit.domain.lecture.dto.LectureResponse;
import com.getit.domain.lecture.service.LectureService;
import com.getit.domain.member.entity.Member;
import com.getit.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lectures")
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;
    private final MemberRepository memberRepository;

    @GetMapping
    public ResponseEntity<List<LectureResponse>> getLectures(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String track
    ) {
        Member member = memberRepository.findByStudentId(Long.valueOf(userDetails.getUsername()))
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        List<LectureResponse> response = lectureService.getLectures(member, track);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{lectureId}/complete")
    public ResponseEntity<Void> completeVideo(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long lectureId
    ) {
        Member member = memberRepository.findByStudentId(Long.valueOf(userDetails.getUsername()))
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        lectureService.completeVideo(member, lectureId);

        return ResponseEntity.ok().build();
    }
}