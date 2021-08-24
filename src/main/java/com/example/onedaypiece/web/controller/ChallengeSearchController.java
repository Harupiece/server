package com.example.onedaypiece.web.controller;

import com.example.onedaypiece.service.ChallengeSearchService;
import com.example.onedaypiece.web.domain.challenge.CategoryName;
import com.example.onedaypiece.web.dto.response.challenge.ChallengeListResponseDto;
import com.example.onedaypiece.web.dto.response.search.ChallengeTitleSearchResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChallengeSearchController {

    private final ChallengeSearchService challengeSearchService;

    @GetMapping("/api/guest/search/{page}/{searchWords}") // 제목 검색
    public ChallengeListResponseDto getChallengeSearchResult(@PathVariable int page,
                                                             @PathVariable String searchWords) {
        return challengeSearchService.getChallengeSearchResult(searchWords, page);
    }

    @GetMapping("/api/guest/search/{categoryName}/{period}/{page}")
    public ChallengeListResponseDto getChallengeSearchByCategoryNameAndPeriod(@PathVariable String categoryName,
                                                                              @PathVariable int period,
                                                                              @PathVariable int page) {
        return challengeSearchService.getChallengeByCategoryNameAndPeriod(categoryName, period, page);
    }
}
