package com.example.onedaypiece.web.dto.response.mypage;


import com.example.onedaypiece.web.domain.challenge.CategoryName;
import com.example.onedaypiece.web.domain.challenge.Challenge;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MypageChallengeResponseDto {

    private Long challengeId;
    private String challengeTitle;
    private String challengeContent;
    private CategoryName categoryName;
    private String challengePassword;
    private Long challengeProgress;
    private String challengeImgUrl;
    private Long challengeMember;

    public MypageChallengeResponseDto(Challenge challenge){
        this.challengeId = challenge.getChallengeId();
        this.challengeTitle = challenge.getChallengeTitle();
        this.challengeContent = challenge.getChallengeContent();
        this.categoryName = challenge.getCategoryName();
        this.challengePassword = challenge.getChallengePassword();
        this.challengeProgress = challenge.getChallengeProgress();
        this.challengeImgUrl = challenge.getChallengeImgUrl();
        this.challengeMember  = challenge.getMember().getMemberId();
    }
}
