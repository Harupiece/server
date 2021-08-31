package com.example.onedaypiece.web.dto.response.mypage.histroy;

import com.example.onedaypiece.web.domain.point.Point;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
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
    private Long acquiredPoint;
    private Point point; // 나중에지우기 불필요 Repository도 쿼리문에서 제외시키기


    @QueryProjection
    public MemberHistoryDto(Long pointHistoryId, LocalDateTime createdAt, String challengeTitle, Long getPoint, Long memberId, String nickname, String profileImg, Long acquiredPoint, Point point) {
        this.pointHistoryId = pointHistoryId;
        this.createdAt = createdAt;
        this.challengeTitle = challengeTitle;
        this.getPoint = getPoint;
        this.memberId = memberId;
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.acquiredPoint = acquiredPoint;
        this.point = point;
    }
}
