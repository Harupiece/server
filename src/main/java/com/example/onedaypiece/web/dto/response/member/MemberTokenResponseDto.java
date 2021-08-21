package com.example.onedaypiece.web.dto.response.member;


import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.dto.response.token.TokenDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.servlet.http.Cookie;

@Getter
@NoArgsConstructor
public class MemberTokenResponseDto {
    private String grantType;
    private Cookie accessToken;
    private Cookie refreshToken;
    private Long accessTokenExpiresIn;
    private MemberResponseDto userInfo;


//    public MemberTokenResponseDto(TokenDto tokenDto, Member member, int challengeCount, int completeChallengeCount){
//        this.grantType = tokenDto.getGrantType();
//        this.accessToken = tokenDto.getAccessToken();
//        this.refreshToken = tokenDto.getRefreshToken();
//        this.accessTokenExpiresIn = tokenDto.getAccessTokenExpiresIn();
//        this.userInfo = new MemberResponseDto(member, challengeCount, completeChallengeCount);
//    }

    public MemberTokenResponseDto(TokenDto tokenDto, Member member, int challengeCount, int completeChallengeCount,
                                  Cookie acCookie, Cookie reCookie){
        this.grantType = tokenDto.getGrantType();
        this.accessToken = acCookie;
        this.refreshToken = reCookie;
        this.accessTokenExpiresIn = tokenDto.getAccessTokenExpiresIn();
        this.userInfo = new MemberResponseDto(member, challengeCount, completeChallengeCount);
    }



}
