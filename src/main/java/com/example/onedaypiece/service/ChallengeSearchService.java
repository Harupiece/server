package com.example.onedaypiece.service;

import com.example.onedaypiece.web.domain.challenge.CategoryName;
import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.challenge.ChallengeQueryRepository;
import com.example.onedaypiece.web.domain.challenge.ChallengeRepository;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecordQueryRepository;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecordRepository;
import com.example.onedaypiece.web.dto.response.challenge.ChallengeListResponseDto;
import com.example.onedaypiece.web.dto.response.challenge.ChallengeResponseDto;
import com.example.onedaypiece.web.dto.response.search.ChallengeTitleSearchResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChallengeSearchService {

    private final ChallengeRecordQueryRepository challengeRecordQueryRepository;
    private final ChallengeQueryRepository challengeQueryRepository;

    final int pageSize = 999;

    public ChallengeListResponseDto getChallengeByCategoryName(CategoryName categoryName, int page) {
        List<Challenge> challengeList = challengeQueryRepository.
                findAllByCategoryName(categoryName, PageRequest.of(page - 1, pageSize));
        return listResponseDtoSource(challengeList);
    }

    public ChallengeListResponseDto getChallengeSearchResult(String searchWords, int page) {
        List<Challenge> challengeList = challengeQueryRepository.
                findAllByWords(
                        searchWords.trim(), PageRequest.of(page - 1, pageSize));
        return listResponseDtoSource(challengeList);
    }

    private ChallengeListResponseDto listResponseDtoSource(List<Challenge> challengeList) {
        ChallengeListResponseDto listResponseDto = new ChallengeListResponseDto();

        List<ChallengeRecord> recordList = challengeRecordQueryRepository.findAllByChallengeIdList(challengeList
                .stream()
                .map(Challenge::getChallengeId)
                .collect(Collectors.toList()));

        for (Challenge challenge : challengeList) {
            List<Long> memberIdList = recordList
                    .stream()
                    .map(r -> r.getMember().getMemberId())
                    .collect(Collectors.toList());

            ChallengeResponseDto responseDto = new ChallengeResponseDto(challenge, memberIdList);
            listResponseDto.addResult(responseDto);
        }

        return listResponseDto;
    }
}
