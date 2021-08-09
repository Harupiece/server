package com.example.onedaypiece.web.dto.request.mypage;


import com.example.onedaypiece.exception.ApiRequestException;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class ProfileUpdateRequestDto {

    private String nickname;
    private String profileImage;

    public ProfileUpdateRequestDto(String nickname, String profileImage){

        if(nickname.trim().contains(" ") ){
            throw new ApiRequestException("닉네임 공백을 포함할수없습니다.");
        }

        if(nickname == null || nickname.isEmpty()){
            throw new ApiRequestException("닉네임을 입력해주세요");
        }

        if(nickname.length() > 20){
            throw new ApiRequestException("닉네임은 20글자를 초과할 수 없습니다.");
        }

        if(profileImage == null || profileImage.isEmpty()){
            throw new ApiRequestException("프로필이미지는 공백이거나 null은 불가능합니다.");
        }

        this.nickname = nickname;
        this.profileImage = profileImage;
    }
}
