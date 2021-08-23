package com.example.onedaypiece.web.dto.response.mypage.histroy;


import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.dto.response.member.MemberResponseDto;
import com.example.onedaypiece.web.dto.response.mypage.CalculLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class MemberHistoryResponseDto {

    private Long memberId;
    private String nickname;
    private String profileImage;
    private Long point;
    private Long level; // 멤버 레벨 계산해서하기
    private int rank;

    private List<PointHistoryDto> postingGetpoint;
    private List<PointHistoryDto> challengeGetpoint;

    public MemberHistoryResponseDto(Member member, List<PointHistoryDto> postingGetpoint, List<PointHistoryDto> challengeGetpoint, int rank) {
        this.memberId = member.getMemberId();
        this.nickname = member.getNickname();
        this.profileImage = member.getProfileImg();
        this.point = member.getPoint().getAcquiredPoint();
        this.level = CalculLevel.calculLevel(member.getPoint().getAcquiredPoint());
        // 포인트히스토리
        this.postingGetpoint = postingGetpoint;
        this.challengeGetpoint = challengeGetpoint;

        this.rank = rank;
    }
}
