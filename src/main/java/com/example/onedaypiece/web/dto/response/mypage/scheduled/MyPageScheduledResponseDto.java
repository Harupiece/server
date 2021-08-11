package com.example.onedaypiece.web.dto.response.mypage.scheduled;

import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.dto.response.mypage.CalculLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class MyPageScheduledResponseDto {

    private Long memberId;
    private String nickname;
    private String profileImage;
    private Long point;
    private Long level;
    private List<ScheduledResponseDto> challengeList;

    public MyPageScheduledResponseDto(Member member, List<ScheduledResponseDto> challengeList){
        this.memberId = member.getMemberId();
        this.nickname = member.getNickname();
        this.profileImage = member.getProfileImg();
        this.point = member.getPoint().getAcquiredPoint();
        this.challengeList = challengeList;
        this.level = CalculLevel.calculLevel(member.getPoint().getAcquiredPoint());
    }

}
