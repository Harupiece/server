package com.example.onedaypiece.web.domain.challenge;

import com.example.onedaypiece.web.domain.common.Timestamped;
import com.example.onedaypiece.web.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Challenge extends Timestamped {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long challengeId;

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
    private boolean challengeStatus; // 삭제 여부

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

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void setChallengeStatus(boolean challengeStatus) {
        this.challengeStatus = challengeStatus;
    }

    public void setChallengeProgress(Long challengeProgress) {
        this.challengeProgress = challengeProgress;
    }

    public Challenge(Long challengeId,
                     String challengeTitle,
                     String challengeContent,
                     CategoryName categoryName,
                     String challengePassword,
                     LocalDateTime challengeStartDate,
                     LocalDateTime challengeEndDate,
                     String challengeImgUrl,
                     String challengeGood,
                     String challengeBad,
                     String challengeHoliday,
                     Member member) {
        this.challengeId = challengeId;
        this.challengeTitle = challengeTitle;
        this.challengeContent = challengeContent;
        this.categoryName = categoryName;
        this.challengePassword = challengePassword;
        this.challengeStartDate = challengeStartDate;
        this.challengeEndDate = challengeEndDate;
        this.challengeStatus = true;
        this.challengeProgress = 1L;
        this.challengeImgUrl = challengeImgUrl;
        this.challengeGood = challengeGood;
        this.challengeBad = challengeBad;
        this.challengeHoliday = challengeHoliday;
        this.member = member;
    }
}
