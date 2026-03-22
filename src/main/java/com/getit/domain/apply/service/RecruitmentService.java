package com.getit.domain.apply.service;

import com.getit.domain.apply.entity.Recruitment;
import com.getit.domain.apply.repository.RecruitmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitmentService {

    private final RecruitmentRepository recruitmentRepository;

    public boolean isOpen() {
        return recruitmentRepository.findTopByOrderByIdDesc()
                .map(Recruitment::isOpen)
                .orElse(false);
    }

    @Transactional
    public void updatePeriod(LocalDateTime startAt, LocalDateTime endAt) {
        recruitmentRepository.findTopByOrderByIdDesc()
                .ifPresentOrElse(
                        r -> r.update(startAt, endAt),
                        () -> recruitmentRepository.save(
                                Recruitment.builder()
                                        .startAt(startAt)
                                        .endAt(endAt)
                                        .build()
                        )
                );
    }
}