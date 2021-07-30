package com.example.onedaypiece.web.dto.response.mypage;


import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@Getter
public class MyPageResponseDto {

    private Long memberId;
    private String nickname;
    private String profileImg;
    private Long point;
    private List<MypageChallengeResponseDto> challengeList;

    public MyPageResponseDto(Member member, Long totalPoint, List<MypageChallengeResponseDto> challengeList){
        this.memberId = member.getMemberId();
        this.nickname = member.getNickname();
        this.profileImg = member.getProfileImg();
        this.point = totalPoint;
        this.challengeList = challengeList;
    }
}
