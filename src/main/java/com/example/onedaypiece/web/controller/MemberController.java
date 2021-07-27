package com.example.onedaypiece.web.controller;

import com.example.onedaypiece.service.MemberService;
import com.example.onedaypiece.web.dto.request.login.LoginRequestDto;
import com.example.onedaypiece.web.dto.request.mypage.MyPageRequestDto;
import com.example.onedaypiece.web.dto.request.signup.SignupRequestDto;
import com.example.onedaypiece.web.dto.request.token.TokenRequestDto;
import com.example.onedaypiece.web.dto.response.mypage.MyPageResponseDto;
import com.example.onedaypiece.web.dto.response.token.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/member")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    //회원가입
    @PostMapping("/signup")
    public void registerUser(@RequestBody SignupRequestDto signupRequestDto){
        memberService.registMember(signupRequestDto);
    }

    // 로그인
    @PostMapping("/login")
    public TokenDto login(@RequestBody LoginRequestDto loginRequestDto){
        return memberService.loginMember(loginRequestDto);
    }

    // 재발급
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(memberService.reissue(tokenRequestDto));
    }


    // 마이 페이지 상세
    @GetMapping("/mypage")
    public ResponseEntity<MyPageResponseDto> getMyPageInfo(@AuthenticationPrincipal UserDetails userDetails){

        MyPageResponseDto responseDto = memberService.getMypageInfo(userDetails.getUsername());
        return ResponseEntity.ok().body(responseDto);
    }

    // 마이 페이지 수정
    @PutMapping("/mypage")
    public ResponseEntity<Void> updateMyPageInfo(@RequestBody MyPageRequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails){

        memberService.updateMember(requestDto, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

}
