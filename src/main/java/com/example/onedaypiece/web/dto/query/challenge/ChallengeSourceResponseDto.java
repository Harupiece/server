package com.example.onedaypiece.web.dto.query.challenge;

import com.example.onedaypiece.web.domain.challenge.CategoryName;
import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class ChallengeSourceResponseDto {

    private Long challengeId;
    private String challengeTitle;
    private CategoryName categoryName; // enum
    private LocalDateTime challengeStartDate;
    private LocalDateTime challengeEndDate;
    private String challengeImgUrl;
    private String tag;
    private Set<Long> challengeMember;

    @QueryProjection
    public ChallengeSourceResponseDto(Challenge challenge, List<ChallengeRecord> records) {
        this.challengeId = challenge.getChallengeId();
        this.challengeTitle = challenge.getChallengeTitle();
        this.categoryName = challenge.getCategoryName();
        this.challengeStartDate = challenge.getChallengeStartDate();
        this.challengeEndDate = challenge.getChallengeEndDate();
        this.challengeImgUrl = challenge.getChallengeImgUrl();
        this.challengeMember = records
                .stream()
                .filter(r -> r.getChallenge().equals(challenge))
                .map(r -> r.getMember().getMemberId())
                .collect(Collectors.toSet());
        this.tag = challenge.getTag();
    }
}
