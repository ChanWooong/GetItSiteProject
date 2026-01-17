package com.getit.domain.lecture.entity;

import com.getit.domain.lecture.Track;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Lecture {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer week;
    private String title;
    // 영상은 우리가 만들어 넣어야됨
    @Enumerated(EnumType.STRING)
    private Track track;
}
