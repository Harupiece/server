package com.example.onedaypiece.web.dto.request.signup;

import com.example.onedaypiece.exception.ApiRequestException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class SignupRequestDto {

    private String email;
    private String nickname;
    private String password;
    private String passwordConfirm;
    private String profileImg;


    public SignupRequestDto(String email,  String password, String nickname, String passwordConfirm,
                            String profileImg){

        if(email == null || email.isEmpty()) {
            throw new ApiRequestException("email(ID)를 입력해주세요");
        }

        // email형식인지 확인하는 정규식 넣기
        if(!email.matches("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$")){
            throw new ApiRequestException("올바른 이메일 형식이 아닙니다.");
        }

        if(email.length() > 40){
            throw new ApiRequestException("이메일이 40글자 초과입니다.");
        }

        if(password == null || passwordConfirm == null){
            throw new ApiRequestException("패스워드가 null이므로 입력해 주세요.");
        }

        if(password.isEmpty() || passwordConfirm.isEmpty()) {
            throw new ApiRequestException("패스워드를 입력해 주세요.");
        }

        if(!(password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,20}$"))){
            throw new ApiRequestException("비밀번호는 8~20 자리이며 숫자 문자 특수문자를 한번씩 포함해야하며 공백이 들어가면안됩니다.");
        }

        if ( !(password.equals(passwordConfirm))){
            throw new ApiRequestException("비밀번호가 서로같지않습니다.");
        }

        if(nickname.trim().contains(" ") ){
            throw new ApiRequestException("닉네임 공백을 포함할수없습니다.");
        }

        if(nickname == null || nickname.isEmpty()){
            throw new ApiRequestException("닉네임을 입력해주세요");
        }

        if(nickname.length() > 20){
            throw new ApiRequestException("닉네임은 20글자를 초과할 수 없습니다.");
        }


        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.profileImg = profileImg;
    }
}
