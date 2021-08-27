package com.example.onedaypiece.web.dto.response.challenge;

import com.example.onedaypiece.web.domain.challenge.CategoryName;
import com.example.onedaypiece.web.domain.challenge.Challenge;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class ChallengeDetailResponseDto {

    private Long challengeId;
    private Long memberId;
    private String memberName;
    private String challengeTitle;
    private String challengeContent;
    private CategoryName categoryName;
    private String challengePassword;
    private LocalDateTime challengeStartDate;
    private LocalDateTime challengeEndDate;
    private Long challengeProgress;
    private String challengeImgUrl;
    private String challengeGood;
    private String challengeBad;
    private String challengeHoliday;
    private String tag;
    private List<ChallengeDetailResponseDtoMemberDto> challengeMember = new ArrayList<>();

    @Builder
    public ChallengeDetailResponseDto(Long challengeId,
                                      Long memberId,
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
                                      List<ChallengeDetailResponseDtoMemberDto> challengeMember) {
        this.challengeId = challengeId;
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

    public static ChallengeDetailResponseDto createChallengeDetailResponseDto(
            Challenge challenge,
            List<ChallengeDetailResponseDtoMemberDto> challengeMember) {

        return ChallengeDetailResponseDto.builder()
                .challengeId(challenge.getChallengeId())
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
