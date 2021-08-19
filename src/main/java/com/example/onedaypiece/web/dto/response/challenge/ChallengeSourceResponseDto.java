package com.example.onedaypiece.web.dto.response.challenge;

import com.example.onedaypiece.web.domain.challenge.CategoryName;
import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
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
    private Set<Long> challengeMember;
    private final List<String> tagList = new ArrayList<>();

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
        if (ChronoUnit.DAYS.between(challenge.getChallengeStartDate(), challenge.getChallengeEndDate()) <= 7) {
            tagList.add("#1주");
        } else if (ChronoUnit.DAYS.between(challenge.getChallengeStartDate(), challenge.getChallengeEndDate()) <= 14) {
            tagList.add("#2주");
        } else if (ChronoUnit.DAYS.between(challenge.getChallengeStartDate(), challenge.getChallengeEndDate()) <= 21) {
            tagList.add("#3주");
        } else {
            tagList.add("#4주 이상");
        }
    }
}
