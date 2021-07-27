package com.example.onedaypiece.web.dto.request.mypage;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// setter없이돌아가 확인해보기
@Setter
@AllArgsConstructor
@Getter
public class MyPageRequestDto {

    private String password;
    private String nickname;
    private String profileImg;
}
