package com.example.onedaypiece.web.dto.response.member;


import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.dto.response.token.TokenDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberTokenResponseDto {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresIn;
    private MemberResponseDto userInfo;

    public MemberTokenResponseDto(TokenDto tokenDto, Member member, int challengeCount, int completeChallengeCount, int rank){
        this.grantType = tokenDto.getGrantType();
        this.accessToken = tokenDto.getAccessToken();
        this.refreshToken = tokenDto.getRefreshToken();
        this.accessTokenExpiresIn = tokenDto.getAccessTokenExpiresIn();
        this.userInfo = new MemberResponseDto(member, challengeCount, completeChallengeCount, rank);
    }

}
