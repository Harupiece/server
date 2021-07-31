package com.example.onedaypiece.web.dto.response.search;

import com.example.onedaypiece.web.domain.challenge.CategoryName;
import com.example.onedaypiece.web.domain.challenge.Challenge;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ChallengeTitleSearchResponseDto {
    private String challengeTitle;
    private CategoryName categoryName;

    public ChallengeTitleSearchResponseDto(Challenge challenge) {
        this.challengeTitle = challenge.getChallengeTitle();
        this.categoryName = challenge.getCategoryName();
    }
}
