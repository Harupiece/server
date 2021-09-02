package com.example.onedaypiece.web.dto.response.mypage.histroy;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MemberHistoryDto {

    private Long pointHistoryId;
    private LocalDateTime createdAt;
    private String challengeTitle;
    private Long getPoint;
    private Long memberId;
    private String nickname;
    private String profileImg;


    @QueryProjection
    public MemberHistoryDto(Long pointHistoryId, LocalDateTime createdAt, String challengeTitle, Long getPoint, Long memberId, String nickname, String profileImg) {
        this.pointHistoryId = pointHistoryId;
        this.createdAt = createdAt;
        this.challengeTitle = challengeTitle;
        this.getPoint = getPoint;
        this.memberId = memberId;
        this.nickname = nickname;
        this.profileImg = profileImg;
    }
}
