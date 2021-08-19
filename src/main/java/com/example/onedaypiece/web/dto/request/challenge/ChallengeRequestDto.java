package com.example.onedaypiece.web.dto.request.challenge;

import com.example.onedaypiece.exception.ApiRequestException;
import com.example.onedaypiece.web.domain.challenge.CategoryName;
import lombok.Getter;
import lombok.Setter;

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

        if(challengeTitle == null || challengeTitle.isEmpty()){
            throw new ApiRequestException("제목이 비었습니다.");
        }

        if(challengeContent == null || challengeContent.isEmpty()){
            throw new ApiRequestException("내용이 비었습니다.");
        }

        if(challengeImgUrl == null || challengeImgUrl.isEmpty()){
            throw new ApiRequestException("챌린지 이미지가 비었습니다.");
        }

        if(challengeGood == null || challengeGood.isEmpty()){
            throw new ApiRequestException("좋은예시가 비었습니다.");
        }

        if(challengeBad == null || challengeBad.isEmpty()){
            throw new ApiRequestException("나쁜예시가 비었습니다.");
        }

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
