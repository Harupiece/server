package com.example.onedaypiece.web.dto.request.challenge;

import com.example.onedaypiece.web.domain.challenge.CategoryName;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChallengeRequestDto {
    private String challengeTitle;
    private String challengeContent;
    private String challengePassword;
    private CategoryName categoryName;
    private LocalDateTime challengeStartDate;
    private LocalDateTime challengeEndDate;
    private String challengeImgUrl;
    private String challengeGood;
    private String challengeBad;
    private String challengeHoliday;


    public ChallengeRequestDto(String challengeTitle, String challengeContent, String challengePassword, CategoryName categoryName,
                               LocalDateTime challengeStartDate, LocalDateTime challengeEndDate, String challengeImgUrl,
                               String challengeGood, String challengeBad, String challengeHoliday){
        this.challengeTitle = challengeTitle;
        this.challengeContent = challengeContent;
        this.challengePassword = challengePassword;
        this.categoryName = categoryName;
        this.challengeStartDate = challengeStartDate;
        this.challengeEndDate = challengeEndDate;
        this.challengeImgUrl = challengeImgUrl;
        this.challengeGood = challengeGood;
        this.challengeBad = challengeBad;
        this.challengeHoliday = challengeHoliday;
    }
}
