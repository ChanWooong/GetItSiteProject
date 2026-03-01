package com.getit.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum College {
    HUMANITIES("인문대학"),
    SOCIAL_SCIENCES("사회과학대학"),
    NATURAL_SCIENCES("자연과학대학"),
    ECONOMICS_BUSINESS("경상대학"),
    ENGINEERING("공과대학"),
    IT("IT대학"),
    AGRICULTURE_LIFE_SCIENCES("농업생명과학대학"),
    ARTS("예술대학"),
    EDUCATION("사범대학"),
    ECOLOGY_HOUSEHOLD("생활과학대학"),
    NURSING("간호대학"),
    PHARMACY("약학대학"),
    MEDICINE("의과대학"),
    DENTISTRY("치과대학"),
    VETERINARY_MEDICINE("수의과대학"),
    CONVERGENCE_TECH("첨단기술융합대학"),
    ECOLOGY_ENVIRONMENT("생태환경대학"),
    SCIENCE_TECH("과학기술대학"),
    PUBLIC_ADMIN("행정학부"),
    GLOBAL_DAEGU("자율전공학부"), // 대구캠퍼스 자율전공
    FUTURE_TALENT("자율미래인재학부"); // 자율미래인재

    private final String description;
}