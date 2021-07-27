package com.example.onedaypiece.web.domain.challenge;

import com.example.onedaypiece.web.domain.common.Timestamped;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.dto.request.challenge.ChallengeRequestDto;
import com.example.onedaypiece.web.dto.request.challenge.PutChallengeRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Challenge extends Timestamped {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long challengeId;

    @Column(nullable = false)
    private String challengeTitle;

    @Column(nullable = false)
    private String challengeContent;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CategoryName categoryName;

    @Column(nullable = false)
    private String challengePassword;

    @Column(nullable = false)
    private LocalDateTime challengeStartDate;

    @Column(nullable = false)
    private LocalDateTime challengeEndDate;

    @Column(nullable = false)
    private boolean challengeStatus; // 삭제 여부

    @Column(nullable = false)
    private Long challengeProgress;

    @Column
    private String challengeImgUrl;

    @Column
    private String challengeGood;

    @Column
    private String challengeBad;

    @Column
    private String challengeHoliday;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "member_id")
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
        this.categoryName = requestDto.getCategoryName();
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
