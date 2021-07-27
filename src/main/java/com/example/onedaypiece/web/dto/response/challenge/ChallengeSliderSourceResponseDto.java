package com.example.onedaypiece.web.dto.response.challenge;

import com.example.onedaypiece.web.domain.challenge.CategoryName;
import com.example.onedaypiece.web.domain.challenge.Challenge;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
public class ChallengeSliderSourceResponseDto {

    private Long challengeId;
    private String challengeTitle;
    private CategoryName categoryName; // enum
    private LocalDateTime challengeStartDate;
    private LocalDateTime challengeEndDate;
    private String challengeImgUrl;
    private List<Long> challengeMember;

    public ChallengeSliderSourceResponseDto(Challenge challenge,
                                            List<Long> challengeMember) {
        this.challengeId = challenge.getChallengeId();
        this.challengeTitle = challenge.getChallengeTitle();
        this.categoryName = challenge.getCategoryName();
        this.challengeStartDate = challenge.getChallengeStartDate();
        this.challengeEndDate = challenge.getChallengeEndDate();
        this.challengeImgUrl = challenge.getChallengeImgUrl();
        this.challengeMember = challengeMember;
    }
}
