package com.example.onedaypiece.web.domain.challenge;

import com.example.onedaypiece.web.domain.common.Timestamped;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.dto.request.challenge.ChallengeRequestDto;
import com.example.onedaypiece.web.dto.request.challenge.PutChallengeRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(indexes = {@Index(name = "idx_status_progress", columnList = "challenge_status, challenge_progress"),
        @Index(name = "idx_status", columnList = "challenge_status")})
public class Challenge extends Timestamped {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
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

    @Column
    private String challengeImgUrl;

    @Column
    private String challengeGood;

    @Column
    private String challengeBad;

    @Column
    private String challengeHoliday;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public void setChallengeStatus(boolean challengeStatus) {
        this.challengeStatus = challengeStatus;
    }

    public void setChallengeProgress(Long challengeProgress) {
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
