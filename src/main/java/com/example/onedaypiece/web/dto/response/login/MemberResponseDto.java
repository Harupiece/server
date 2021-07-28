package com.example.onedaypiece.web.dto.response.login;


import com.example.onedaypiece.web.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MemberResponseDto {
    private Long memberId;
    private String nickname;
    private String profileImg;
    private Long point;

    public MemberResponseDto(Member member, Long memberPoint){
        this.memberId = member.getMemberId();
        this.nickname = member.getNickname();
        this.profileImg = member.getProfileImg();
        this.point = memberPoint;
    }
}
