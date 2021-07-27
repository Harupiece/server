package com.example.onedaypiece.web.dto.response.mypage;


import com.example.onedaypiece.web.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
public class MyPageResponseDto {

    private Long memberId;
    private String password; // 이걸왜줌??
    private String nickname;
    private String profileImg;
    private Long point; // 이게문제군...

    public MyPageResponseDto(Member member, Long totalPoint){
        this.memberId = member.getMemberId();
        this.password = member.getPassword();
        this.nickname = member.getNickname();
        this.profileImg = member.getProfileImg();
        this.point = totalPoint;
    }
}
