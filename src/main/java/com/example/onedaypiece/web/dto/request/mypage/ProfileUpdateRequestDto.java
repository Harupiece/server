package com.example.onedaypiece.web.dto.request.mypage;


import com.example.onedaypiece.exception.ApiRequestException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

@Slf4j
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
