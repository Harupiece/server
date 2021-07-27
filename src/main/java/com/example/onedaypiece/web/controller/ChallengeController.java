package com.example.onedaypiece.web.controller;

import com.example.onedaypiece.service.ChallengeService;
import com.example.onedaypiece.web.dto.request.challenge.ChallengeRequestDto;
import com.example.onedaypiece.web.dto.response.challenge.ChallengeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class ChallengeController {

    private final ChallengeService challengeService;

    @GetMapping("/api/member/challenge/{challengeId}")
    public ChallengeResponseDto getChallengeDetail(@PathVariable Long challengeId) {
        return challengeService.getChallengeDetail(challengeId);
    }

    @PostMapping("/api/member/challenge")
    public Map<String, String> createChallenge(@RequestBody ChallengeRequestDto requestDto) {
        return challengeService.createChallenge(requestDto);
    }

    @DeleteMapping("/api/member/challenge/{challengeId}")
    public Map<String, String> deleteChallenge(@PathVariable Long challengeId) {
        return challengeService.deleteChallenge(challengeId);
    }
}
