package com.example.onedaypiece.web.dto.request.mypage;


import com.example.onedaypiece.exception.ApiRequestException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotNull;


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileUpdateRequestDto {

    @NotNull(message = "닉네임은 null이거나 빈값은 불가능합니다.")
    private String nickname;
    @NotNull(message = "프로필이미지는 null이거나 빈값은 불가능합니다. ")
    private String profileImage;

    public ProfileUpdateRequestDto(String nickname, String profileImage){

        if(nickname.isEmpty()){
            throw new ApiRequestException("변경할 닉네임을 입력해주세요.");
        }
        
        if(profileImage.isEmpty()){
            throw new ApiRequestException("프로필 이미지를 넣어주세요.");
        }

        this.nickname = nickname;
        this.profileImage = profileImage;
    }
}
