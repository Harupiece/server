package com.example.onedaypiece.web.dto.response.challenge;

import com.example.onedaypiece.web.domain.challenge.CategoryName;
import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class ChallengeSourceResponseDto {

    private Long challengeId;
    private String challengeTitle;
    private CategoryName categoryName;
    private LocalDateTime challengeStartDate;
    private LocalDateTime challengeEndDate;
    private String challengeImgUrl;
    private Set<Long> challengeMember;
    private String tag;

    @Builder
    public ChallengeSourceResponseDto(Long challengeId,
                                      String challengeTitle,
                                      CategoryName categoryName,
                                      LocalDateTime challengeStartDate,
                                      LocalDateTime challengeEndDate,
                                      String challengeImgUrl,
                                      Set<Long> challengeMember, String tag) {
        this.challengeId = challengeId;
        this.challengeTitle = challengeTitle;
        this.categoryName = categoryName;
        this.challengeStartDate = challengeStartDate;
        this.challengeEndDate = challengeEndDate;
        this.challengeImgUrl = challengeImgUrl;
        this.challengeMember = challengeMember;
        this.tag = tag;
    }

    public static ChallengeSourceResponseDto createChallengeSourceResponseDto(Challenge challenge,
                                                                              List<ChallengeRecord> records) {
        return ChallengeSourceResponseDto.builder()
                .challengeId(challenge.getChallengeId())
                .challengeTitle(challenge.getChallengeTitle())
                .categoryName(challenge.getCategoryName())
                .challengeStartDate(challenge.getChallengeStartDate())
                .challengeEndDate(challenge.getChallengeEndDate())
                .challengeImgUrl(challenge.getChallengeImgUrl())
                .challengeMember(records
                        .stream()
                        .filter(r -> r.getChallenge().equals(challenge))
                        .map(r -> r.getMember().getMemberId())
                        .collect(Collectors.toSet()))
                .build();
    }
}
