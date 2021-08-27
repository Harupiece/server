package com.example.onedaypiece.web.controller;

import com.example.onedaypiece.service.ChallengeRecordService;
import com.example.onedaypiece.web.dto.request.challengeRecord.ChallengeRecordRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ChallengeRecordController {

    private final ChallengeRecordService challengeRecordService;

    @PostMapping("/api/member/challenge-request") // 챌린지 신청
    public void requestChallenge(@RequestBody ChallengeRecordRequestDto requestDto,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        challengeRecordService.requestChallenge(requestDto, userDetails.getUsername());
    }

    @DeleteMapping("/api/member/challenge-give-up/{challengeId}") // 챌린지 포기
    public void giveUpChallenge(@PathVariable Long challengeId,
                                @AuthenticationPrincipal UserDetails userDetails) {
        challengeRecordService.giveUpChallenge(challengeId, userDetails.getUsername());
    }

    @PostMapping("/api/member/challenge-pre-start/{challengeId}") // 챌린지 바로 시작
    public void preStartChallenge(@PathVariable Long challengeId,
                                  @AuthenticationPrincipal UserDetails userDetails) {
        challengeRecordService.preStartChallenge(challengeId, userDetails.getUsername());
    }
}