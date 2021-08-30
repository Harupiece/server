package com.example.onedaypiece.web.controller;

import com.example.onedaypiece.service.ChallengeService;
import com.example.onedaypiece.web.dto.request.challenge.ChallengeRequestDto;
import com.example.onedaypiece.web.dto.request.challenge.PutChallengeRequestDto;
import com.example.onedaypiece.web.dto.response.challenge.ChallengeDetailResponseDto;
import com.example.onedaypiece.web.dto.response.challenge.ChallengeMainResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ChallengeController {

    private final ChallengeService challengeService;

    /**
     * 1. 비로그인 메인 페이지
     */
    @GetMapping("/api/guest/main")
    public ChallengeMainResponseDto getGuestMainChallengeDetail() {
        return challengeService.getMainPage();
    }

    /**
     * 2. 챌린지 상세
     */
    @GetMapping("/api/guest/challenge/{challengeId}")
    public ChallengeDetailResponseDto getChallengeDetail(@PathVariable Long challengeId) {
        return challengeService.getChallengeDetail(challengeId);
    }

    /**
     * 3. 챌린지 등록
     */
    @PostMapping("/api/member/challenge")
    public Long createChallenge(@RequestBody ChallengeRequestDto requestDto,
                                @AuthenticationPrincipal UserDetails userDetails) {
        return challengeService.postChallenge(requestDto, userDetails.getUsername());
    }

    /**
     * 4. 챌린지 수정
     */
    @PutMapping("/api/member/challenge")
    public void putChallenge(@RequestBody PutChallengeRequestDto requestDto,
                             @AuthenticationPrincipal UserDetails userDetails) {
        challengeService.putChallenge(requestDto, userDetails.getUsername());
    }

    /**
     * 5. 챌린지 취소
     */
    @DeleteMapping("/api/member/challenge/{challengeId}")
    public void deleteChallenge(@PathVariable Long challengeId, @AuthenticationPrincipal UserDetails userDetails) {
        challengeService.deleteChallenge(challengeId, userDetails.getUsername());
    }
}
