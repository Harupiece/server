package com.example.onedaypiece.service;

import com.example.onedaypiece.web.dto.response.mypage.histroy.PointHistoryResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class PointHistoryTest {

    private Long pointHistoryId;
    private LocalDate createdAt;
    private String challengeTitle;
    private Long point;


    public PointHistoryTest(PointHistoryResponseDto o) {
        this.pointHistoryId = o.getPointHistoryId();
        this.createdAt = o.getCreatedAt();
        this.challengeTitle=o.getChallengeTitle();
        this.point=o.getPoint();

    }

    public PointHistoryTest(PostingTestDto postingTestDto) {
        this.pointHistoryId = postingTestDto.getPointHistoryId();
        this.createdAt = postingTestDto.getCreatedAt().toLocalDate();
        this.challengeTitle= postingTestDto.getChallengeTitle();
        this.point=postingTestDto.getGetPoint();
    }
}
