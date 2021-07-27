package com.example.onedaypiece.web.dto.response.challenge;

import com.example.onedaypiece.web.domain.challenge.CategoryName;

import java.time.LocalDateTime;
import java.util.List;

public class ChallengeResponseDto {

    private final String memberName; // 챌린지 만든 사람
    private final String challengeTitle;
    private final String challengeContent;
    private final CategoryName categoryName; // enum
    private final String challengePassword;
    private final LocalDateTime challengeStartDate;
    private final LocalDateTime challengeEndDate;
    private final Long challengeProgress;
    private final String challengeImgUrl;
    private final String challengeGood;
    private final String challengeBad;
    private final String challengeHoliday;
    private final List<Long> challengeMember;

    public ChallengeResponseDto(String memberName, String challengeTitle, String challengeContent,
                                CategoryName categoryName, String challengePassword, LocalDateTime challengeStartDate,
                                LocalDateTime challengeEndDate, Long challengeProgress, String challengeImgUrl,
                                String challengeGood, String challengeBad, String challengeHoliday,
                                List<Long> challengeMember) {
        this.memberName = memberName;
        this.challengeTitle = challengeTitle;
        this.challengeContent = challengeContent;
        this.categoryName = categoryName;
        this.challengePassword = challengePassword;
        this.challengeStartDate = challengeStartDate;
        this.challengeEndDate = challengeEndDate;
        this.challengeProgress = challengeProgress;
        this.challengeImgUrl = challengeImgUrl;
        this.challengeGood = challengeGood;
        this.challengeBad = challengeBad;
        this.challengeHoliday = challengeHoliday;
        this.challengeMember = challengeMember;
    }
}
