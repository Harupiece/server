package com.example.onedaypiece.web.controller;

import com.example.onedaypiece.service.ChallengeRecordService;
import com.example.onedaypiece.web.dto.request.challengeRecord.ChallengeRecordRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class ChallengeRecordController {

    private final ChallengeRecordService challengeRecordService;

    @PostMapping("/api/member/challenge-request") // 챌린지 신청
    public Map<String, String> requestChallenge(@RequestBody ChallengeRecordRequestDto requestDto,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        return challengeRecordService.requestChallenge(requestDto, userDetails);
    }

    @DeleteMapping("/api/member/challenge-give-up/{challengeId}") // 챌린지 포기
    public void giveUpChallenge(@PathVariable Long challengeId) {
        challengeRecordService.giveUpChallenge(challengeId);
    }
}
