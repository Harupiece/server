package com.example.onedaypiece.web.dto.response.mypage.histroy;


import com.example.onedaypiece.service.PostingTestDto;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.point.Point;
import com.example.onedaypiece.web.domain.pointhistory.PointHistory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class PointHistoryResponseDto {

    private Long pointHistoryId;
    private LocalDate createdAt;
    private String challengeTitle;
    private Long point;
    private Point pointObject;
    @JsonIgnore
    private Member member;
    private Long memberId;
    private String nickname;
    private String profileImg;
    private Long acquiredPoint;

    public PointHistoryResponseDto(PointHistory pointHistory){
        this.pointHistoryId = pointHistory.getPointhistoryId();
        this.createdAt = changeLocalDate(pointHistory.getCreatedAt());
        this.challengeTitle = pointHistory.getCertification().getPosting().getChallenge().getChallengeTitle();
        this.point = pointHistory.getGetPoint();
        this.member = pointHistory.getCertification().getMember();

    }

    // 테스트
    public PointHistoryResponseDto(PostingTestDto postingTestDto) {
        this.pointHistoryId = postingTestDto.getPointHistoryId();
        this.createdAt =changeLocalDate(postingTestDto.getCreatedAt());
        this.challengeTitle = postingTestDto.getChallengeTitle();
        this.point = postingTestDto.getGetPoint();
        this.pointObject = postingTestDto.getPoint();
        this.memberId = postingTestDto.getMemberId();
        this.nickname =postingTestDto.getNickname();
        this.profileImg =postingTestDto.getProfileImg();
        this.acquiredPoint = postingTestDto.getAcquiredPoint();

    }


    public LocalDate changeLocalDate(LocalDateTime localDateTime){
        return localDateTime.toLocalDate();
    }
}
