package com.example.onedaypiece.web.dto.response.login;


import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.dto.response.token.TokenDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponseDto {

    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresIn;
    private MemberResponseDto userInfo;

    public LoginResponseDto(TokenDto tokenDto, Member member, Long totalPoint){
        this.grantType = tokenDto.getGrantType();
        this.accessToken = tokenDto.getAccessToken();
        this.refreshToken = tokenDto.getRefreshToken();
        this.accessTokenExpiresIn = tokenDto.getAccessTokenExpiresIn();
        this.userInfo = new MemberResponseDto(member,totalPoint);
    }
}
