package com.example.onedaypiece.web.dto.response.mypage.histroy;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PointHistoryDto {

    private Long pointHistoryId;
    private LocalDate createdAt;
    private String challengeTitle;
    private Long getPoint;

    public PointHistoryDto(MemberHistoryDto dto){
        this.pointHistoryId = dto.getPointHistoryId();
        this.createdAt = changeLocalDate(dto.getCreatedAt());
        this.challengeTitle = dto.getChallengeTitle();
        this.getPoint = dto.getGetPoint();
    }

    public LocalDate changeLocalDate(LocalDateTime localDateTime){
        return localDateTime.toLocalDate();
    }
}
