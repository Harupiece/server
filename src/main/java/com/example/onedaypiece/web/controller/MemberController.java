package com.example.onedaypiece.web.controller;

import com.example.onedaypiece.service.MemberService;
import com.example.onedaypiece.web.dto.request.login.LoginRequestDto;
import com.example.onedaypiece.web.dto.request.mypage.ProfileUpdateRequestDto;
import com.example.onedaypiece.web.dto.request.mypage.PwUpdateRequestDto;
import com.example.onedaypiece.web.dto.request.signup.SignupRequestDto;
import com.example.onedaypiece.web.dto.request.token.TokenRequestDto;
import com.example.onedaypiece.web.dto.response.member.MemberTokenResponseDto;
import com.example.onedaypiece.web.dto.response.member.reload.ReloadResponseDto;
import com.example.onedaypiece.web.dto.response.mypage.MyPageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/member")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    /**
     * 1. 회원가입
     */
    @PostMapping("/signup")
    public void registerUser(@RequestBody SignupRequestDto signupRequestDto) {
        memberService.registMember(signupRequestDto);
    }

    /**
     * 2. 로그인 요청사항으로 수정
     */
    @PostMapping("/login")
    public MemberTokenResponseDto login(@RequestBody LoginRequestDto loginRequestDto) {
        return memberService.loginMember(loginRequestDto);
    }

    /**
     * 3. 새로고침
     */
    @GetMapping("/reload")
    public ReloadResponseDto reload(@AuthenticationPrincipal UserDetails userDetails) {
        return memberService.reload(userDetails.getUsername());
    }

    /**
     * 4. 재발급
     */
    @PostMapping("/reissue")
    public MemberTokenResponseDto reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return memberService.reissue(tokenRequestDto);
    }

    /**
     * 5. 마이 페이지 수정 비밀번호 변경만
     */
    @PutMapping("/mypage/password")
    public void updateMyPageInfoPassword(@RequestBody PwUpdateRequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails) {
        memberService.updatePassword(requestDto, userDetails.getUsername());
    }

    /**
     * 6. 마이 페이지 수정 프로필 + 닉네임
     */
    @PutMapping("/mypage/profile")
    public String updateMyPageInfoProfile(@RequestBody ProfileUpdateRequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails) {
        return memberService.updateProfile(requestDto, userDetails.getUsername());
    }

    /**
     * 7. 마이 페이지
     */
    @GetMapping("/mypage")
    public MyPageResponseDto getMypage(@AuthenticationPrincipal UserDetails userDetails) {
        return memberService.getMyPage(userDetails.getUsername());
    }
}
