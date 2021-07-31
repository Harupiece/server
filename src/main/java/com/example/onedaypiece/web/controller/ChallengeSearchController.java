package com.example.onedaypiece.web.controller;

import com.example.onedaypiece.service.ChallengeSearchService;
import com.example.onedaypiece.web.domain.challenge.CategoryName;
import com.example.onedaypiece.web.dto.response.challenge.ChallengeListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChallengeSearchController {

    private final ChallengeSearchService challengeSearchService;

    @GetMapping("/api/guest/challenge/category/{page}/{categoryName}") // 카테고리 별 조회
    public ResponseEntity<ChallengeListResponseDto> getChallengeByCategoryName(@PathVariable int page,
                                                                               @PathVariable CategoryName categoryName) {
        return ResponseEntity.ok().body(challengeSearchService.getChallengeByCategoryName(categoryName, page));
    }

    @GetMapping("/api/guest/search/{page}/{searchWords}") // 제목 검색
    public ResponseEntity<ChallengeListResponseDto> getChallengeSearchResult(@PathVariable int page,
                                                                             @PathVariable String searchWords) {
        return ResponseEntity.ok().body(challengeSearchService.getChallengeSearchResult(searchWords, page));
    }
}
