package com.example.onedaypiece.web.dto.request.mypage;


import com.example.onedaypiece.exception.ApiRequestException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileUpdateRequestDto {

    @NotBlank(message = "닉네임은 null이거나 빈값은 불가능합니다.")
    private String nickname;
    @NotBlank(message = "프로필이미지는 null이거나 빈값은 불가능합니다. ")
    private String profileImage;

    public ProfileUpdateRequestDto(String nickname, String profileImage){
        this.nickname = nickname;
        this.profileImage = profileImage;
    }
}
