package com.example.onedaypiece.web.dto.response.reload;


import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.dto.response.login.MemberResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReloadResponseDto {

    private MemberResponseDto userInfo;

    public ReloadResponseDto(Member member, Long totalPoint){
        this.userInfo = new MemberResponseDto(member,totalPoint);
    }
}
