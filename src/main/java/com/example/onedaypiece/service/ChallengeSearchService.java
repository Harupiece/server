package com.example.onedaypiece.service;

import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.challenge.ChallengeQueryRepository;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecordQueryRepository;
import com.example.onedaypiece.web.dto.response.challenge.ChallengeListResponseDto;
import com.example.onedaypiece.web.dto.response.challenge.ChallengeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.onedaypiece.web.dto.response.challenge.ChallengeResponseDto.createChallengeResponseDto;
import static org.springframework.util.CollectionUtils.isEmpty;

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
                findAllByWords(searchWords.trim(), PageRequest.of(page - 1, SEARCH_SIZE));

        Map<Challenge, List<ChallengeRecord>> recordMap = challengeRecordQueryRepository
                .findAllByChallengeList(challengeList)
                .stream()
                .collect(Collectors.groupingBy(ChallengeRecord::getChallenge));

        return getChallengeListResponseDto(challengeList, recordMap);
    }

    /**
     * 챌린지 카테고리, 태그별 검색
     */
    public ChallengeListResponseDto getChallengeBySearch(String categoryName,
                                                         int period,
                                                         int progress,
                                                         int page) {
        Pageable pageable = PageRequest.of(page - 1, SEARCH_SIZE);
        Slice<Challenge> challengeList = challengeQueryRepository
                .findAllBySearch(categoryName, period, progress, pageable);

        Map<Challenge, List<ChallengeRecord>> recordMap = challengeRecordQueryRepository
                .findAllByChallengeList(challengeList).stream()
                .collect(Collectors.groupingBy(ChallengeRecord::getChallenge));

        return getChallengeListResponseDto(challengeList, recordMap);
    }

    private ChallengeListResponseDto getChallengeListResponseDto(Slice<Challenge> challengeList,
                                                                 Map<Challenge, List<ChallengeRecord>> recordMap) {
        List<ChallengeResponseDto> challengeDtoList = challengeList
                .stream()
                .filter(c -> !isEmpty(recordMap.get(c)))
                .map(c -> createChallengeResponseDto(c, getRecordListEqualsCurrentChallenge(c, recordMap.get(c))))
                .collect(Collectors.toList());

        return ChallengeListResponseDto.createChallengeListDto(challengeDtoList, challengeList.hasNext());
    }

    private Set<Long> getRecordListEqualsCurrentChallenge(Challenge challenge, List<ChallengeRecord> recordList) {
        return recordList
                .stream()
                .filter(r -> r.getChallenge().equals(challenge))
                .map(r -> r.getMember().getMemberId())
                .collect(Collectors.toSet());
    }
}
