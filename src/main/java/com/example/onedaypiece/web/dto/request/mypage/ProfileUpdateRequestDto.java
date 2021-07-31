package com.example.onedaypiece.web.dto.request.mypage;


import com.example.onedaypiece.exception.ApiRequestException;
import lombok.Getter;

@Getter
public class ProfileUpdateRequestDto {

    private String nickname;
    private String profileImage;

    public ProfileUpdateRequestDto(String nickname, String profileImage){


        if(nickname.isEmpty()){
            throw new ApiRequestException("변경할 닉네임을 입력해주세요.");
        }

        this.nickname = nickname;
        this.profileImage = profileImage;
    }
}
