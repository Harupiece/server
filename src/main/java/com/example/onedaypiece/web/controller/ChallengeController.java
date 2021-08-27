package com.example.onedaypiece.web.controller;

import com.example.onedaypiece.service.ChallengeService;
import com.example.onedaypiece.util.Scheduler;
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
    private final Scheduler scheduler;

    @GetMapping("/api/guest/main") // 비로그인 메인 페이지
    public ChallengeMainResponseDto getGuestMainChallengeDetail() {
        return challengeService.getMainPage();
    }

    @GetMapping("/api/guest/challenge/{challengeId}") // 챌린지 상세
    public ChallengeDetailResponseDto getChallengeDetail(@PathVariable Long challengeId) {
        return challengeService.getChallengeDetail(challengeId);
    }

    @PostMapping("/api/member/challenge") // 챌린지 등록
    public Long createChallenge(@RequestBody ChallengeRequestDto requestDto,
                                @AuthenticationPrincipal UserDetails userDetails) {
        return challengeService.postChallenge(requestDto, userDetails.getUsername());
    }

    @PutMapping("/api/member/challenge") // 챌린지 수정
    public void putChallenge(@RequestBody PutChallengeRequestDto requestDto,
                             @AuthenticationPrincipal UserDetails userDetails) {
        challengeService.putChallenge(requestDto, userDetails.getUsername());
    }

    @DeleteMapping("/api/member/challenge/{challengeId}") // 챌린지 취소 (유저에겐 삭제, 관리자 입장에선 상태 true->false)
    public void deleteChallenge(@PathVariable Long challengeId, @AuthenticationPrincipal UserDetails userDetails) {
        challengeService.deleteChallenge(challengeId, userDetails.getUsername());
    }

    @PostMapping("/api/member/challenge/official") // 챌린지 등록
    public void createOfficialChallenge() {
        scheduler.createOfficialChallenge();
    }
}
