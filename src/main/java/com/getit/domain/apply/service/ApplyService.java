package com.getit.domain.apply.service;

import com.getit.domain.apply.entity.Application;
import com.getit.domain.apply.dto.ApplyDraftDataDto;
import com.getit.domain.apply.dto.ApplyDraftRequest;
import com.getit.domain.apply.dto.ApplyRequest;
import com.getit.domain.apply.repository.ApplicationRepository;
import com.getit.domain.member.entity.Member;
import com.getit.domain.member.repository.MemberRepository;
import com.getit.global.exception.ErrorCode;
import com.getit.global.exception.GlobalExceptionManager.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplyService {

    private final ApplicationRepository applicationRepository;
    private final MemberRepository memberRepository;

    private Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Transactional
    public void submit(Long memberId, ApplyRequest request) {
        Member member = findById(memberId);
        boolean alreadySubmitted = applicationRepository.existsByMemberAndIsDraftFalse(member);
        if(alreadySubmitted){
            throw new BusinessException(ErrorCode.ALREADY_SUBMITTED_APPLY);
        }
        Application application = Application.builder()
                .member(member)
                .answer1(request.getAnswer1())
                .answer2(request.getAnswer2())
                .answer3(request.getAnswer3())
                .answer4(request.getAnswer4())
                .answer5(request.getAnswer5())
                .answer6(request.getAnswer6())
                .answer7(request.getAnswer7())
                .isDraft(false)
                .build();

        applicationRepository.save(application);
        applicationRepository.deleteByMemberAndIsDraftTrue(member);
    }

    public boolean hasSubmitted(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        return applicationRepository.existsByMemberAndIsDraftFalse(member);
    }

    @Transactional
    public void saveDraft(Long memberId, ApplyDraftRequest request) {
        Member member = findById(memberId);

        String a1 = nullToEmpty(request.getAnswer1());
        String a2 = nullToEmpty(request.getAnswer2());
        String a3 = nullToEmpty(request.getAnswer3());
        String a4 = nullToEmpty(request.getAnswer4());
        String a5 = request.getAnswer5() != null ? request.getAnswer5() : "";
        String a6 = nullToEmpty(request.getAnswer6());
        String a7 = nullToEmpty(request.getAnswer7());


        applicationRepository.findFirstByMemberAndIsDraftTrueOrderByIdDesc(member)
                .ifPresentOrElse(
                        draft -> {
                            draft.updateDraftContent(a1, a2, a3, a4, a5, a6, a7);
                            applicationRepository.deleteByMemberAndIsDraftTrueAndIdNot(member, draft.getId());
                        },
                        () -> {
                            Application application = Application.builder()
                                    .member(member)
                                    .answer1(a1)
                                    .answer2(a2)
                                    .answer3(a3)
                                    .answer4(a4)
                                    .answer5(a5)
                                    .answer6(a6)
                                    .answer7(a7)
                                    .isDraft(true)
                                    .build();
                            applicationRepository.save(application);
                            applicationRepository.deleteByMemberAndIsDraftTrueAndIdNot(member, application.getId());
                        }
                );
    }

    private static String nullToEmpty(String s) {
        return s != null ? s : "";
    }

    public Optional<ApplyDraftDataDto> getDraft(Long memberId) {
        Member member = findById(memberId);

        return applicationRepository.findFirstByMemberAndIsDraftTrueOrderByIdDesc(member)
                .map(draft -> ApplyDraftDataDto.builder()
                        .answer1(draft.getAnswer1())
                        .answer2(draft.getAnswer2())
                        .answer3(draft.getAnswer3())
                        .answer4(draft.getAnswer4())
                        .answer5(draft.getAnswer5())
                        .agree(false)
                        .build());
    }
}
