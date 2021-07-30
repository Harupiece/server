package com.example.onedaypiece.service;

import com.example.onedaypiece.exception.ApiRequestException;
import com.example.onedaypiece.web.domain.challenge.CategoryName;
import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.challenge.ChallengeRepository;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecordRepository;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.member.MemberRepository;
import com.example.onedaypiece.web.dto.request.challenge.ChallengeRequestDto;
import com.example.onedaypiece.web.dto.request.challenge.PutChallengeRequestDto;
import com.example.onedaypiece.web.dto.response.challenge.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.onedaypiece.web.domain.challenge.CategoryName.*;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeRecordRepository challengeRecordRepository;
    private final MemberRepository memberRepository;

    private final LocalDateTime currentLocalDateTime = LocalDateTime.now();

    public ChallengeResponseDto getChallengeDetail(Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(
                () -> new ApiRequestException("존재하지 않는 챌린지id입니다"));

        challengeProgressChecker(challenge);

        List<Long> challengeMember = new ArrayList<>();
        challengeRecordRepository.findAllByChallenge(challenge).forEach(
                value -> challengeMember.add(value.getMember().getMemberId()));

        return new ChallengeResponseDto(challenge, challengeMember);
    }

    @Transactional
    public void deleteChallenge(Long challengeId, String username) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(
                () -> new ApiRequestException("존재하지 않는 챌린지 id입니다"));

        if (!challenge.getMember().getEmail().equals(username)) {
            throw new IllegalArgumentException("작성자가 아닙니다.");
        }

        LocalDateTime currentLocalDateTime = LocalDateTime.now();
        if (currentLocalDateTime.isBefore(challenge.getChallengeStartDate())) {
            challenge.setChallengeStatus(false);
            challenge.setChallengeProgress(3L);
        } else {
            throw new ApiRequestException("이미 시작된 챌린지는 삭제할 수 없습니다.");
        }

        challengeRecordRepository.deleteAllByChallenge(challenge); // 챌린지 참가 기록 삭제
    }

    @Transactional
    public void createChallenge(ChallengeRequestDto requestDto, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiRequestException("존재하지 않는 유저입니다."));

        challengeRepository.findAllByMember(member)
                .forEach(challenge -> challengeRepository.save(challengeProgressCheckerReturner(challenge)));

        challengeRepository.findAllByMember(member)
                .stream()
                .filter(value -> value.getCategoryName() == requestDto.getCategoryName())
                .forEach(value -> {
                    throw new ApiRequestException("이미 해당 카테고리에 챌린지를 생성한 유저입니다.");
                });

        if (requestDto.getChallengePassword().length() < 4) {
            if (!requestDto.getChallengePassword().equals("")) {
                throw new ApiRequestException("비밀번호는 4자리 이상으로 설정해야합니다.");
            }
        }

        Challenge challenge = new Challenge(requestDto, member);
        challengeRepository.save(challenge);
        challengeRecordRepository.save(new ChallengeRecord(challenge, member));
    }

    @Transactional
    public void putChallenge(PutChallengeRequestDto requestDto, String username) {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new ApiRequestException("존재하지 않는 유저입니다."));
        Challenge challenge = challengeRepository.findByChallengeIdAndMember(requestDto.getChallengeId(), member)
                .orElseThrow(() -> new ApiRequestException("존재하지 않는 챌린지입니다."));
        challengeProgressChecker(challenge);
        if (!challenge.getChallengeProgress().equals(1L)) {
            throw new ApiRequestException("이미 시작되거나 종료된 챌린지입니다.");
        }
        if (!challenge.isChallengeStatus()) {
            throw new ApiRequestException("삭제된 챌린지입니다.");
        }

        Map<CategoryName, Integer> participatingCategoryMap = Arrays
                .stream(values())
                .collect(Collectors.toMap(categoryName -> categoryName, categoryName -> 0, (a, b) -> b));

        List<CategoryName> participatingCategoryList = challengeRepository.findAllByMember(member)
                .stream()
                .map(Challenge::getCategoryName)
                .collect(Collectors.toList());

        participatingCategoryMap
                .put(requestDto.getCategoryName(), participatingCategoryMap.get(requestDto.getCategoryName()) + 1);

        for (CategoryName category : participatingCategoryList) {
            participatingCategoryMap.put(category, participatingCategoryMap.get(category) + 1);
            if (requestDto.getCategoryName() == category && participatingCategoryMap.get(requestDto.getCategoryName()) == 2) {
                throw new ApiRequestException("동일한 카테고리에 2개 이상의 챌린지를 개설할 수 없습니다.");
            }
        }

        challenge.putChallenge(requestDto);
    }

    @Transactional
    public void deleteChallengeByAdmin(Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ApiRequestException("존재하지 않는 챌린지입니다."));
        challengeRepository.deleteById(challengeId);
        challengeRecordRepository.deleteAllByChallenge(challenge);
    }

    // Guest 메인 페이지

    public ChallengeGuestMainResponseDto getGuestMainChallengeDetail() {
        ChallengeGuestMainResponseDto mainRequestDto = new ChallengeGuestMainResponseDto();

        responseDtoRefactor(mainRequestDto, 0);

        categoryCollector(EXERCISE).forEach(mainRequestDto::addExercise);
        categoryCollector(LIVINGHABITS).forEach(mainRequestDto::addLivingHabits);
        categoryCollector(STUDY).forEach(mainRequestDto::addStudy);
        categoryCollector(MONEY).forEach(mainRequestDto::addMoney);
        return mainRequestDto;
    }
    // Member 메인 페이지

    public ChallengeMemberMainResponseDto getMemberMainChallengeDetail(String userEmail) {
        ChallengeMemberMainResponseDto mainRequestDto = new ChallengeMemberMainResponseDto();
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ApiRequestException("존재하지 않는 유저입니다."));

        List<ChallengeRecord> myChallengeList = challengeRecordRepository.findAllByMember(member);

        // myList 추가
        for (ChallengeRecord mine : myChallengeList) {
            List<Long> myChallengeMemberList = new ArrayList<>();
            challengeRecordRepository.findAllByChallenge(mine.getChallenge()).forEach(
                    record -> myChallengeMemberList.add(record.getMember().getMemberId()));
            mainRequestDto.addMyList(new ChallengeSliderSourceResponseDto(mine.getChallenge(), myChallengeMemberList));
        }

        final int userSliderSize = myChallengeList.size();

        responseDtoRefactor(mainRequestDto, userSliderSize);

        categoryCollector(EXERCISE).forEach(mainRequestDto::addExercise);
        categoryCollector(LIVINGHABITS).forEach(mainRequestDto::addLivingHabits);
        categoryCollector(STUDY).forEach(mainRequestDto::addStudy);
        categoryCollector(MONEY).forEach(mainRequestDto::addMoney);
        return mainRequestDto;
    }

    private List<ChallengeSliderSourceResponseDto> categoryCollector(CategoryName categoryName) {

        final int categorySize = 3;

        List<ChallengeSliderSourceResponseDto> returnList = new ArrayList<>();


        challengeRepository.findAllByCategoryNameOrderByModifiedAtDescListed(categoryName)
                .forEach(challenge -> challengeRepository.save(challengeProgressCheckerReturner(challenge)));

        Page<Challenge> pagedChallengeList = challengeRepository
                .findAllByCategoryNameOrderByModifiedAtDescPaged(categoryName, PageRequest.of(0, categorySize));

        for (Challenge challenge : pagedChallengeList) {
            List<Long> memberIdList = new ArrayList<>();
            challengeRecordRepository.findAllByChallenge(challenge)
                    .forEach(record -> memberIdList.add(record.getMember().getMemberId()));
            returnList.add(new ChallengeSliderSourceResponseDto(challenge, memberIdList));
        }

        return returnList;
    }

    private void responseDtoRefactor(ChallengeGuestMainResponseDto mainResponseDto, int minusSize) {

        final int sliderSize = 5;

        HashSet<Long> currentChallengeIdList = new HashSet<>();

        List<ChallengeRecord> recordList = challengeRecordRepository.findAll();
        recordList.forEach(
                record -> currentChallengeIdList.add(record.getChallenge().getChallengeId()));

        Map<Long, Integer> popularList = new HashMap<>();

        // 여긴 시간복잡도가 N^2
        for (Long challengeId : currentChallengeIdList) {
            if (!popularList.containsKey(challengeId)) {
                popularList.put(challengeId, 0);
            }
            recordList.stream().filter(record -> record.getChallenge().getChallengeId().equals(challengeId)).forEach(
                    record -> popularList.put(challengeId, popularList.get(challengeId) + 1));
        }

        List<Map.Entry<Long, Integer>> beSortedList = new LinkedList<>(popularList.entrySet());

        Collections.sort(beSortedList, (o1, o2) -> {
            if (o1.getValue() > o2.getValue()) {
                return -1;
            } else if (o1.getValue() < o2.getValue()) {
                return 1;
            }
            return o1.getKey().compareTo(o2.getKey());
        });

        Map<Long, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Long, Integer> entry : beSortedList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        for (Long id : sortedMap.keySet()) {
            Challenge challenge = challengeRepository.getById(id);
            List<Long> memberIdList = new ArrayList<>();
            challengeRecordRepository.findAllByChallenge(challenge).forEach(
                    record -> memberIdList.add(record.getMember().getMemberId()));
            ChallengeSliderSourceResponseDto sliderRequestDto =
                    new ChallengeSliderSourceResponseDto(challenge, memberIdList);
            mainResponseDto.addPopular(sliderRequestDto);
            if (mainResponseDto.getPopular().size() > sliderSize - minusSize) {
                break;
            }
        }
    }

    public List<Challenge> getAllChallenge() {
        return challengeRepository.findAll();
    }

    public ChallengeListResponseDto getChallengeByCategoryName(CategoryName categoryName, int page) {
        final int pageSize = 6;

        Page<Challenge> challengeList = challengeRepository.
                findAllByCategoryNameOrderByModifiedAtDescPaged(categoryName, PageRequest.of(page - 1, pageSize));
        return listResponseDtoSource(challengeList);
    }

    public ChallengeListResponseDto getChallengeSearchResult(String searchWords, int page) {
        final int searchPageSize = 6;

        Page<Challenge> challengeList = challengeRepository.
                findAllByWordsAndChallengeStatusTrueOrderByModifiedAtDesc(
                        searchWords.trim(), PageRequest.of(page - 1, searchPageSize));
        return listResponseDtoSource(challengeList);
    }

    private ChallengeListResponseDto listResponseDtoSource(Page<Challenge> challengeList) {
        ChallengeListResponseDto listResponseDto = new ChallengeListResponseDto();
        for (Challenge challenge : challengeList) {
            List<Long> memberIdList = new ArrayList<>();
            challengeRecordRepository.findAllByChallenge(challenge).forEach(
                    record -> memberIdList.add(record.getMember().getMemberId()));
            listResponseDto.addResult(new ChallengeResponseDto(challenge, memberIdList));
        }
        return listResponseDto;
    }

    private void challengeProgressChecker(Challenge challenge) {
        if (currentLocalDateTime.isBefore(challenge.getChallengeStartDate())) {
            challenge.setChallengeProgress(1L);
        } else if (currentLocalDateTime.isBefore(challenge.getChallengeEndDate())) {
            challenge.setChallengeProgress(2L);
        } else {
            challenge.setChallengeProgress(3L);
            challengeRecordRepository.findAllByChallenge(challenge)
                    .forEach(ChallengeRecord::setChallengeRecordStatusToFalse);
        }
    }

    @Transactional
    Challenge challengeProgressCheckerReturner(Challenge challenge) {
        if (currentLocalDateTime.isBefore(challenge.getChallengeStartDate())) {
            challenge.setChallengeProgress(1L);
        } else if (currentLocalDateTime.isBefore(challenge.getChallengeEndDate())) {
            challenge.setChallengeProgress(2L);
        } else {
            challenge.setChallengeProgress(3L);
            challengeRecordRepository.findAllByChallenge(challenge)
                    .forEach(ChallengeRecord::setChallengeRecordStatusToFalse);
        }

        return challenge;
    }
}
