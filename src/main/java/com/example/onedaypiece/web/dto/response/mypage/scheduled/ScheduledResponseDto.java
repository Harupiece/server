package com.example.onedaypiece.web.dto.response.mypage.scheduled;

import com.example.onedaypiece.web.domain.challenge.CategoryName;
import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@NoArgsConstructor
@Getter
public class ScheduledResponseDto {

    private Long challengeId;
    private String challengeTitle;
    private String challengeContent;
    private CategoryName categoryName;
    private Long challengeProgress;
    private String challengeImgUrl;
    private Long challengeMember;
    private LocalDateTime challengeStartDate;
    private LocalDateTime challengeEndDate;

    private int successPercent;  // 진행율
    private List<String> participateImg;
    private long participateSize;


    public ScheduledResponseDto(Challenge challenge, List<ChallengeRecord> participate){
        this.challengeId = challenge.getChallengeId();
        this.challengeTitle = challenge.getChallengeTitle();
        this.challengeContent = challenge.getChallengeContent();
        this.categoryName = challenge.getCategoryName();
        this.challengeProgress = challenge.getChallengeProgress();
        this.challengeImgUrl = challenge.getChallengeImgUrl();
        this.challengeMember  = challenge.getMember().getMemberId();
        this.challengeStartDate = challenge.getChallengeStartDate();
        this.challengeEndDate = challenge.getChallengeEndDate();

        this.participateImg = getImg(participate);
        this.successPercent = 0;
        this.participateSize = participateImg.size();

    }

    public List<String> getImg(List<ChallengeRecord> recordList){
        List<String> imageList = recordList.stream()
                .map(challengeRecord -> challengeRecord.getMember().getProfileImg())
                .collect(Collectors.toList());
        return imageList;
    }


}
