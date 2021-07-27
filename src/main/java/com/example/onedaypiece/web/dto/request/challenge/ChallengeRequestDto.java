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
}
