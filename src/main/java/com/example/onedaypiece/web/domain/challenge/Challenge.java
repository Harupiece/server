package com.example.onedaypiece.web.domain.challenge;

import com.example.onedaypiece.web.domain.common.Timestamped;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
public class Challenge extends Timestamped {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "challenge_id")
    private Long id;

    @Column
    private String challengeTitle;

    @Column
    private String challengeContent;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CategoryName categoryName;

    @Column
    private String challengePassword;

    @Column
    private LocalDateTime challengeStartDate;

    @Column
    private LocalDateTime challengeEndDate;

    @Column
    private boolean challengeStatus;

    @Column
    private Long challengeProgress;

    @Column
    private String challengeImgUrl;

    @Column
    private String challengeGood;

    @Column
    private String challengeBad;

    @Column
    private String challengeHoliday;

//    @ManyToOne
//    @JoinColumn(name = "member_id")
//    private Member member;
//
//    @ManyToOne
//    @JoinColumn(name = "point_id")
//    private Point point;
}
