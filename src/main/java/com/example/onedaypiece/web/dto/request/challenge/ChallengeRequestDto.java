package com.example.onedaypiece.web.dto.request.challenge;

import com.example.onedaypiece.web.domain.challenge.CategoryName;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChallengeRequestDto {
    private final String challengeTitle;
    private final String challengeContent;
    private final String challengePassword;
    private final CategoryName categoryName;
    private final LocalDateTime challengeStartDate;
    private final LocalDateTime challengeEndDate;
    private final String challengeImgUrl;
    private final String challengeGood;
    private final String challengeBad;
    private final String challengeHoliday;

    public ChallengeRequestDto(String challengeTitle, String challengeContent, String challengePassword, CategoryName categoryName,
                               LocalDateTime challengeStartDate, LocalDateTime challengeEndDate, String challengeImgUrl,
                               String challengeGood, String challengeBad, String challengeHoliday){

        this.challengeTitle = challengeTitle;
        this.challengeContent = challengeContent;
        this.challengePassword = challengePassword;
        this.categoryName = categoryName;
        this.challengeStartDate = challengeStartDate.withHour(0).withMinute(0).withSecond(0);
        this.challengeEndDate = challengeEndDate.withHour(23).withMinute(59).withSecond(57);
        this.challengeImgUrl = challengeImgUrl;
        this.challengeGood = challengeGood;
        this.challengeBad = challengeBad;
        this.challengeHoliday = challengeHoliday;
    }
}
