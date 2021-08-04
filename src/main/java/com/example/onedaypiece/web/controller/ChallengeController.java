package com.example.onedaypiece.web.controller;

import com.example.onedaypiece.service.ChallengeService;
import com.example.onedaypiece.web.dto.request.challenge.ChallengeRequestDto;
import com.example.onedaypiece.web.dto.request.challenge.PutChallengeRequestDto;
import com.example.onedaypiece.web.dto.response.challenge.ChallengeMainResponseDto;
import com.example.onedaypiece.web.dto.response.challenge.ChallengeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ChallengeController {

    private final ChallengeService challengeService;

    @GetMapping("/api/guest/main") // 비로그인 메인 페이지
    public ResponseEntity<ChallengeMainResponseDto> getGuestMainChallengeDetail() {
        return ResponseEntity.ok().body(challengeService.getMainPage(""));
    }

    @GetMapping("/api/member/main") // 로그인 메인 페이지
    public ResponseEntity<ChallengeMainResponseDto> getMemberMainChallengeDetail(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok().body(challengeService.getMainPage(userDetails.getUsername()));
    }

    @GetMapping("/api/guest/challenge/{challengeId}") // 챌린지 상세
    public ResponseEntity<ChallengeResponseDto> getChallengeDetail(@PathVariable Long challengeId) {
        return ResponseEntity.ok().body(challengeService.getChallengeDetail(challengeId));
    }

    @PostMapping("/api/member/challenge") // 챌린지 등록
    public ResponseEntity<Long> createChallenge(@RequestBody ChallengeRequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok().body(challengeService.createChallenge(requestDto, userDetails.getUsername()));
    }

    @PutMapping("/api/member/challenge") // 챌린지 수정
    public ResponseEntity<Void> putChallenge(@RequestBody PutChallengeRequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails) {
        challengeService.putChallenge(requestDto, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/member/challenge/{challengeId}") // 챌린지 취소 (유저에겐 삭제, 관리자 입장에선 상태 true->false)
    public ResponseEntity<Void> deleteChallenge(@PathVariable Long challengeId, @AuthenticationPrincipal UserDetails userDetails) {
        challengeService.deleteChallenge(challengeId, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
