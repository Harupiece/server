package com.example.onedaypiece.web.domain.challenge;

import com.example.onedaypiece.web.domain.common.Timestamped;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.dto.request.challenge.ChallengeRequestDto;
import com.example.onedaypiece.web.dto.request.challenge.PutChallengeRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(indexes = {@Index(name = "idx_status_progress", columnList = "challenge_status, challenge_progress"),
        @Index(name = "idx_status", columnList = "challenge_status")})
public class Challenge extends Timestamped implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name="challenge_id")
    private Long challengeId;

    @Column(nullable = false)
    private String challengeTitle;

    @Column(nullable = false)
    private String challengeContent;

    @Column(name = "category_name", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CategoryName categoryName;

    @Column(nullable = false)
    private String challengePassword;

    @Column(nullable = false)
    private LocalDateTime challengeStartDate;

    @Column(nullable = false)
    private LocalDateTime challengeEndDate;

    // 삭제 여부
    @Column(name = "challenge_status", nullable = false)
    private boolean challengeStatus;

    @Column(name = "challenge_progress", nullable = false)
    private Long challengeProgress;

    @Column(length = 3000)
    private String challengeImgUrl;

    @Column(length = 3000)
    private String challengeGood;

    @Column(length = 3000)
    private String challengeBad;

    @Column
    private String challengeHoliday;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;


    @Builder
    public Challenge(Long challengeId, String challengeTitle, String challengeContent, CategoryName categoryName, String challengePassword, LocalDateTime challengeStartDate, LocalDateTime challengeEndDate, boolean challengeStatus, Long challengeProgress, String challengeImgUrl, String challengeGood, String challengeBad, String challengeHoliday, Member member) {
        this.challengeId = challengeId;
        this.challengeTitle = challengeTitle;
        this.challengeContent = challengeContent;
        this.categoryName = categoryName;
        this.challengePassword = challengePassword;
        this.challengeStartDate = challengeStartDate;
        this.challengeEndDate = challengeEndDate;
        this.challengeStatus = challengeStatus;
        this.challengeProgress = challengeProgress;
        this.challengeImgUrl = challengeImgUrl;
        this.challengeGood = challengeGood;
        this.challengeBad = challengeBad;
        this.challengeHoliday = challengeHoliday;
        this.member = member;
    }

    public void setChallengeStatusFalse() {
        this.challengeStatus = false;
    }

    public void updateChallengeProgress(Long challengeProgress) {
        this.challengeProgress = challengeProgress;
    }

    public void putChallenge(PutChallengeRequestDto requestDto) {
        this.challengeTitle = requestDto.getChallengeTitle();
        this.challengeContent = requestDto.getChallengeContent();
        this.challengePassword = requestDto.getChallengePassword();
        this.challengeStartDate = requestDto.getChallengeStartDate();
        this.challengeEndDate = requestDto.getChallengeEndDate();
        this.challengeImgUrl = requestDto.getChallengeImgUrl();
        this.challengeGood = requestDto.getChallengeGood();
        this.challengeBad = requestDto.getChallengeBad();
        this.challengeHoliday = requestDto.getChallengeHoliday();
    }

    public Challenge(ChallengeRequestDto requestDto, Member member) {
        this.challengeTitle = requestDto.getChallengeTitle();
        this.challengeContent = requestDto.getChallengeContent();
        this.categoryName = requestDto.getCategoryName();
        this.challengePassword = requestDto.getChallengePassword();
        this.challengeStartDate = requestDto.getChallengeStartDate();
        this.challengeEndDate = requestDto.getChallengeEndDate();
        this.challengeStatus = true;
        this.challengeProgress = 1L;
        this.challengeImgUrl = requestDto.getChallengeImgUrl();
        this.challengeGood = requestDto.getChallengeGood();
        this.challengeBad = requestDto.getChallengeBad();
        this.challengeHoliday = requestDto.getChallengeHoliday();
        this.member = member;
    }
}
