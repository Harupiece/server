package com.example.onedaypiece.web.dto.response.mypage.histroy;


import com.example.onedaypiece.web.domain.pointhistory.PointHistory;
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

    public PointHistoryResponseDto(PointHistory pointHistory){
        this.pointHistoryId = pointHistory.getPointhistoryId();
        this.createdAt = changeLocalDate(pointHistory.getCreatedAt());
        this.challengeTitle = pointHistory.getCertification().getPosting().getChallenge().getChallengeTitle();
        this.point = pointHistory.getGetPoint();
    }

    public LocalDate changeLocalDate(LocalDateTime localDateTime){
        return localDateTime.toLocalDate();
    }
}
