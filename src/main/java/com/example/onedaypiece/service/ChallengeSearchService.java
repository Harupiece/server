package com.example.onedaypiece.service;

import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.challenge.ChallengeQueryRepository;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecordQueryRepository;
import com.example.onedaypiece.web.dto.response.challenge.ChallengeListResponseDto;
import com.example.onedaypiece.web.dto.response.challenge.ChallengeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChallengeSearchService {

    private final ChallengeRecordQueryRepository challengeRecordQueryRepository;
    private final ChallengeQueryRepository challengeQueryRepository;

    final int SEARCH_SIZE = 8;

    /**
     * 챌린지 단어 검색
     */
    public ChallengeListResponseDto getChallengeSearchResult(String searchWords, int page) {
        Slice<Challenge> challengeList = challengeQueryRepository.
                findAllByWords(
                        searchWords.trim(), PageRequest.of(page - 1, SEARCH_SIZE));

        return getChallengeListResponseDto(challengeList);
    }

    /**
     * 챌린지 카테고리, 태그별 검색
     */
    public ChallengeListResponseDto getChallengeByCategoryNameAndPeriod(String categoryName,
                                                                        int period,
                                                                        int progress,
                                                                        int page) {
        Pageable pageable = PageRequest.of(page - 1, SEARCH_SIZE);
        Slice<Challenge> challengeList = challengeQueryRepository
                .findAllByCategoryNameAndPeriod(categoryName, period, progress, pageable);

        return getChallengeListResponseDto(challengeList);
    }

    private ChallengeListResponseDto getChallengeListResponseDto(Slice<Challenge> challengeList) {
        List<ChallengeResponseDto> challengeDtoList = challengeList
                .stream()
                .map(c -> new ChallengeResponseDto(c, getRecordListFromRepository(c)))
                .collect(Collectors.toList());

        return ChallengeListResponseDto.createChallengeListDto(challengeDtoList, challengeList.hasNext());
    }

    private List<Long> getRecordListFromRepository(Challenge challenge) {
        return challengeRecordQueryRepository.findAllByChallenge(challenge)
                .stream()
                .map(r -> r.getMember().getMemberId())
                .collect(Collectors.toList());
    }
}
