package com.example.onedaypiece.web.dto.response.challenge;

import com.example.onedaypiece.web.domain.challenge.CategoryName;
import com.example.onedaypiece.web.domain.challenge.Challenge;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
public class ChallengeResponseDto {

    private Long challengeId;
    private Long memberId;
    private String memberName; // 챌린지 만든 사람
    private String challengeTitle;
    private String challengeContent;
    private CategoryName categoryName; // enum
    private String challengePassword;
    private LocalDateTime challengeStartDate;
    private LocalDateTime challengeEndDate;
    private Long challengeProgress;
    private String challengeImgUrl;
    private String challengeGood;
    private String challengeBad;
    private String challengeHoliday;
    private String tag;
    private Set<Long> challengeMember = new HashSet<>();

    @Builder
    public ChallengeResponseDto(Long memberId,
                                String memberName,
                                String challengeTitle,
                                String challengeContent,
                                CategoryName categoryName,
                                String challengePassword,
                                LocalDateTime challengeStartDate,
                                LocalDateTime challengeEndDate,
                                Long challengeProgress,
                                String challengeImgUrl,
                                String challengeGood,
                                String challengeBad,
                                String challengeHoliday,
                                String tag,
                                Set<Long> challengeMember) {
        this.memberId = memberId;
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
        this.tag = tag;
        this.challengeMember = challengeMember;
    }

    public static ChallengeResponseDto createChallengeResponseDto(Challenge challenge, Set<Long> challengeMember) {
        return ChallengeResponseDto.builder()
                .memberId(challenge.getMember().getMemberId())
                .memberName(challenge.getMember().getNickname())
                .challengeTitle(challenge.getChallengeTitle())
                .challengeContent(challenge.getChallengeContent())
                .categoryName(challenge.getCategoryName())
                .challengePassword(challenge.getChallengePassword())
                .challengeStartDate(challenge.getChallengeStartDate())
                .challengeEndDate(challenge.getChallengeEndDate())
                .challengeProgress(challenge.getChallengeProgress())
                .challengeImgUrl(challenge.getChallengeImgUrl())
                .challengeGood(challenge.getChallengeGood())
                .challengeBad(challenge.getChallengeBad())
                .challengeHoliday(challenge.getChallengeHoliday())
                .tag(challenge.getTag())
                .challengeMember(challengeMember)
                .build();
    }
}
