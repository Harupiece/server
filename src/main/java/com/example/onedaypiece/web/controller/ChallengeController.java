package com.example.onedaypiece.web.controller;

import com.example.onedaypiece.service.ChallengeService;
import com.example.onedaypiece.web.domain.challenge.CategoryName;
import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.dto.request.challenge.ChallengeRequestDto;
import com.example.onedaypiece.web.dto.request.challenge.PutChallengeRequestDto;
import com.example.onedaypiece.web.dto.response.challenge.ChallengeGuestMainResponseDto;
import com.example.onedaypiece.web.dto.response.challenge.ChallengeListResponseDto;
import com.example.onedaypiece.web.dto.response.challenge.ChallengeMemberMainResponseDto;
import com.example.onedaypiece.web.dto.response.challenge.ChallengeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChallengeController {

    private final ChallengeService challengeService;

    @GetMapping("/api/guest/main") // 비로그인 메인 페이지
    public ResponseEntity<ChallengeGuestMainResponseDto> getGuestMainChallengeDetail() {
        return ResponseEntity.ok().body(challengeService.getGuestMainChallengeDetail());
    }

    @GetMapping("/api/member/main") // 로그인 메인 페이지
    public ResponseEntity<ChallengeMemberMainResponseDto> getMemberMainChallengeDetail(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok().body(challengeService.getMemberMainChallengeDetail(userDetails.getUsername()));
    }

    @GetMapping("/api/member/challenge/{challengeId}") // 챌린지 상세
    public ResponseEntity<ChallengeResponseDto> getChallengeDetail(@PathVariable Long challengeId) {
        return ResponseEntity.ok().body(challengeService.getChallengeDetail(challengeId));
    }

    @PostMapping("/api/member/challenge") // 챌린지 등록
    public ResponseEntity<Void> createChallenge(@RequestBody ChallengeRequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails) {
        challengeService.createChallenge(requestDto, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/member/challenge") // 챌린지 수정
    public ResponseEntity<Void> putChallenge(@RequestBody PutChallengeRequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails) {
        challengeService.putChallenge(requestDto, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/member/challenge/{challengeId}") // 챌린지 삭제 (유저에겐 삭제, 관리자 입장에선 상태 true->false)
    public ResponseEntity<Void> deleteChallenge(@PathVariable Long challengeId, @AuthenticationPrincipal UserDetails userDetails) {
        challengeService.deleteChallenge(challengeId, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/admin/challenge/{challengeId}") // admin 권한으로 DB에서 아예 삭제
    public ResponseEntity<Void> deleteChallengeByAdmin(@PathVariable Long challengeId) {
        challengeService.deleteChallengeByAdmin(challengeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/guest/challenge/category/{page}/{categoryName}") // 카테고리 별 조회
    public ResponseEntity<ChallengeListResponseDto> getChallengeByCategoryName(@PathVariable int page,
                                                               @PathVariable CategoryName categoryName) {
        return ResponseEntity.ok().body(challengeService.getChallengeByCategoryName(categoryName, page));
    }

    @GetMapping("/api/guest/search/{page}/{searchWords}") // 제목 검색
    public ResponseEntity<ChallengeListResponseDto> getChallengeSearchResult(@PathVariable int page,
                                                             @PathVariable String searchWords) {
        return ResponseEntity.ok().body(challengeService.getChallengeSearchResult(searchWords, page));
    }

    @GetMapping("/api/admin/challenge") // 전체 조회 (DB 확인용)
    public ResponseEntity<List<Challenge>> getAllChallenge() {
        return ResponseEntity.ok().body(challengeService.getAllChallenge());
    }
}
