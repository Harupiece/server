package com.example.onedaypiece.web.dto.request.mypage;


import com.example.onedaypiece.exception.ApiRequestException;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
public class ProfileUpdateRequestDto {

    private String nickname;
    private String profileImage;

    public ProfileUpdateRequestDto(String nickname, String profileImage){

        if(nickname.isEmpty()){
            throw new ApiRequestException("변경할 닉네임을 입력해주세요.");
        }
        
        if(profileImage.isEmpty()){
            throw new ApiRequestException("프로필 이미지를 넣어주세요.");
        }

        if(profileImage == null){
            throw new ApiRequestException("프로필이미지가 null입니다.");
        }

        this.nickname = nickname;
        this.profileImage = profileImage;
    }
}
