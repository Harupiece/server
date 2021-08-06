package com.example.onedaypiece.service;

import com.example.onedaypiece.web.domain.challenge.CategoryName;
import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.challenge.ChallengeRepository;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecordRepository;
import com.example.onedaypiece.web.dto.response.challenge.ChallengeListResponseDto;
import com.example.onedaypiece.web.dto.response.challenge.ChallengeResponseDto;
import com.example.onedaypiece.web.dto.response.search.ChallengeTitleSearchResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChallengeSearchService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeRecordRepository challengeRecordRepository;

    final static int pageSize = 8;

    public ChallengeListResponseDto getChallengeByCategoryName(CategoryName categoryName, int page) {
        List<Challenge> challengeList = challengeRepository.
                findAllByCategoryNameOrderByModifiedAtDescListed(categoryName, PageRequest.of(page - 1, pageSize));
        return listResponseDtoSource(challengeList);
    }

    public ChallengeListResponseDto getChallengeSearchResult(String searchWords, int page) {
        List<Challenge> challengeList = challengeRepository.
                findAllByWordsAndChallengeStatusTrueOrderByModifiedAtDesc(
                        searchWords.trim(), PageRequest.of(page - 1, pageSize));
        return listResponseDtoSource(challengeList);
    }

    private ChallengeListResponseDto listResponseDtoSource(List<Challenge> challengeList) {
        ChallengeListResponseDto listResponseDto = new ChallengeListResponseDto();
        for (Challenge challenge : challengeList) {
            List<Long> memberIdList = new ArrayList<>();
            challengeRecordRepository.findAllByChallengeId(challenge.getChallengeId()).forEach(
                    record -> memberIdList.add(record.getMember().getMemberId()));
            listResponseDto.addResult(new ChallengeResponseDto(challenge, memberIdList));
        }
        return listResponseDto;
    }

    public List<ChallengeTitleSearchResponseDto> getChallengeTitle(Long progress) {
        return challengeRepository.findAllByChallengeStatusTrueAndChallengeProgressLessThan(progress + 1)
                .stream()
                .map(ChallengeTitleSearchResponseDto::new)
                .collect(Collectors.toList());
    }
}
