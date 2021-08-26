package com.example.onedaypiece.web.controller;

import com.example.onedaypiece.service.ChallengeSearchService;
import com.example.onedaypiece.web.dto.response.challenge.ChallengeListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChallengeSearchController {

    private final ChallengeSearchService challengeSearchService;

    @GetMapping("/api/guest/search/{searchWords}/{page}") // 제목 검색
    public ChallengeListResponseDto getChallengeSearchResult(@PathVariable int page,
                                                             @PathVariable String searchWords) {
        return challengeSearchService.getChallengeSearchResult(searchWords, page);
    }

    @GetMapping("/api/guest/search/{categoryName}/{period}/{progress}/{page}") // 소팅 검색
    public ChallengeListResponseDto getChallengeSearchByCategoryNameAndPeriod(@PathVariable String categoryName,
                                                                              @PathVariable int period,
                                                                              @PathVariable int progress,
                                                                              @PathVariable int page) {
        return challengeSearchService.getChallengeBySearch(categoryName, period, progress, page);
    }
}
