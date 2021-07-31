package com.example.onedaypiece.web.dto.request.mypage;


import com.example.onedaypiece.exception.ApiRequestException;
import lombok.Getter;

@Getter
public class PasswordUpdateRequestDto {

    private String currentPassword;
    private String newPassword;
    private String newPasswordCheck;

    public PasswordUpdateRequestDto(String currentPassword, String newPassword, String newPasswordCheck){

        if(currentPassword.isEmpty()){
            throw new ApiRequestException("현재 비밀번호를 입력해주세요.");
        }

        if(newPassword.isEmpty() || newPasswordCheck.isEmpty()){
            throw new ApiRequestException("변경할 비밀번호를 입력해주세요.");
        }

        if(currentPassword.equals(newPassword)){
            throw new ApiRequestException("변경할비밀번호와 현재 비밀번호가 같습니다.");
        }

        if(newPassword.length() < 4 || newPassword.length() > 20){
            throw new ApiRequestException("변경할 비밀번호는  4~20자리를 사용해야 합니다.");
        }

        if ( !(newPassword.equals(newPasswordCheck))){
            throw new ApiRequestException("새로운 비밀번호와 비밀번호확인이 서로같지않습니다.");
        }

        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.newPasswordCheck = newPasswordCheck;
    }
}
