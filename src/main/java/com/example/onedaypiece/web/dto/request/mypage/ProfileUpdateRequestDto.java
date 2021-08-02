package com.example.onedaypiece.web.dto.request.mypage;


import com.example.onedaypiece.exception.ApiRequestException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

@Slf4j
@Getter
public class ProfileUpdateRequestDto {

    private String nickname;
    private String profileImage;

    public ProfileUpdateRequestDto(String nickname, String profileImage){
        log.info("값들어오는지 Dto에서 확인 닉네임: {}, 프로필이미지: {}", nickname, profileImage);

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
