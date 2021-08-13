package com.example.onedaypiece.web.dto.request.login;

import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequestDto {
    private String email;
    private String password;

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
