package com.example.onedaypiece.web.dto.response.mypage.proceed;


import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.dto.response.mypage.CalculLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@Getter
public class MypageProceedResponseDto {

    private Long memberId;
    private String nickname;
    private String profileImage;
    private Long point;
    private Long level;
    private List<ProceedResponseDto> challengeList;

    @Builder
    public MypageProceedResponseDto(Member member, Long totalPoint, List<ProceedResponseDto> challengeList){
        this.memberId = member.getMemberId();
        this.nickname = member.getNickname();
        this.profileImage = member.getProfileImg();
        this.point = totalPoint;
        this.challengeList = challengeList;
        this.level = CalculLevel.calculLevel(member.getPoint().getAcquiredPoint());
    }

    public static MypageProceedResponseDto createMypageProceedResponseDto(Member member,
                                                                          Long totalPoint,
                                                                          List<ProceedResponseDto> challengeList){

        return MypageProceedResponseDto.builder()
                .member(member)
                .totalPoint(totalPoint)
                .challengeList(challengeList)
                .build();
    }
}
