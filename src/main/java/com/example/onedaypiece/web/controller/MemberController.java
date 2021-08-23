package com.example.onedaypiece.web.controller;

import com.example.onedaypiece.service.MemberService;
import com.example.onedaypiece.web.domain.pointHistory.PointHistory;
import com.example.onedaypiece.web.dto.request.login.LoginRequestDto;
import com.example.onedaypiece.web.dto.request.mypage.ProfileUpdateRequestDto;
import com.example.onedaypiece.web.dto.request.mypage.PwUpdateRequestDto;
import com.example.onedaypiece.web.dto.request.signup.SignupRequestDto;
import com.example.onedaypiece.web.dto.request.token.TokenRequestDto;
import com.example.onedaypiece.web.dto.response.member.MemberTokenResponseDto;
import com.example.onedaypiece.web.dto.response.mypage.MyPageResponseDto;
import com.example.onedaypiece.web.dto.response.mypage.histroy.MemberHistoryResponseDto;
import com.example.onedaypiece.web.dto.response.mypage.end.MyPageEndResponseDto;
import com.example.onedaypiece.web.dto.response.mypage.proceed.MypageProceedResponseDto;
import com.example.onedaypiece.web.dto.response.mypage.scheduled.MyPageScheduledResponseDto;
import com.example.onedaypiece.web.dto.response.member.reload.ReloadResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
    
    // 로그인 요청사항으로수정
    @PostMapping("/login")
    public MemberTokenResponseDto login(@RequestBody LoginRequestDto loginRequestDto){
        return memberService.loginMember(loginRequestDto);
    }

    // 새로고침
    @GetMapping("/reload")
    public ReloadResponseDto reload(@AuthenticationPrincipal UserDetails userDetails){
        return memberService.reload(userDetails.getUsername());
    }

    // 재발급
    @PostMapping("/reissue")
    public ResponseEntity<MemberTokenResponseDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(memberService.reissue(tokenRequestDto));
    }

    // 마이 페이지 수정 비밀번호변경만
    @PutMapping("/mypage/password")
    public ResponseEntity<Void> updateMyPageInfoPassword(@RequestBody PwUpdateRequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails){
        memberService.updatePassword(requestDto, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    // 마이 페이지 수정 프로필 + 닉네임
    @PutMapping("/mypage/profile")
    public ResponseEntity<String> updateMyPageInfoProfile(@RequestBody ProfileUpdateRequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails){
        String afterProfileImg = memberService.updateProfile(requestDto, userDetails.getUsername());
        return ResponseEntity.ok(afterProfileImg);
    }


    // 마이페이지 종합선물세트
    @GetMapping("/mypage")
    public ResponseEntity<MyPageResponseDto> getMypage(@AuthenticationPrincipal UserDetails userDetails){
        MyPageResponseDto responseDto = memberService.getMyPage(userDetails.getUsername());
        return ResponseEntity.ok().body(responseDto);
    }



}
