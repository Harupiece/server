package com.example.onedaypiece.service;

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

import static com.example.onedaypiece.web.domain.challenge.CategoryName.*;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeRecordRepository challengeRecordRepository;
    private final MemberRepository memberRepository;

    public ChallengeResponseDto getChallengeDetail(Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(
                () -> new NullPointerException("존재하지 않은 챌린지id입니다"));

        LocalDateTime currentLocalDateTime = LocalDateTime.now();
        if (currentLocalDateTime.isBefore(challenge.getChallengeStartDate())) {
            challenge.setChallengeProgress(1L);
        } else if (currentLocalDateTime.isBefore(challenge.getChallengeEndDate())) {
            challenge.setChallengeProgress(2L);
        } else {
            challenge.setChallengeProgress(3L);
        }

        List<Long> challengeMember = new ArrayList<>();
        challengeRecordRepository.findAllByChallenge(challenge).forEach(
                value -> challengeMember.add(value.getMember().getMemberId()));

        return new ChallengeResponseDto(challenge, challengeMember);
    }

    @Transactional
    public Map<String, String> deleteChallenge(Long challengeId, String username) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(
                () -> new NullPointerException("존재하지 않은 챌린지id입니다"));

        if (challenge.getMember().getEmail().equals(username)) {
            throw new IllegalArgumentException("작성자가 아닙니다.");
        }

        Map<String, String> deleteResultMap = new HashMap<>();

        LocalDateTime currentLocalDateTime = LocalDateTime.now();
        if (currentLocalDateTime.isBefore(challenge.getChallengeStartDate())) {
            challenge.setChallengeStatus(false);
            challenge.setChallengeProgress(3L);

            deleteResultMap.put("msg", "챌린지가 비활성화되었습니다.");
        } else {
            deleteResultMap.put("msg", "이미 시작된 챌린지는 삭제할 수 없습니다.");
        }

        challengeRecordRepository.deleteAllByChallenge(challenge); // 챌린지 참가 기록 삭제

        return deleteResultMap;
    }

    @Transactional
    public void createChallenge(ChallengeRequestDto requestDto, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NullPointerException("존재하지 않는 유저입니다."));

        challengeRepository.findAllByMember(member)
                .stream()
                .filter(value -> value.getCategoryName() == requestDto.getCategoryName())
                .forEach(value -> {
                    throw new IllegalArgumentException("이미 해당 카테고리에 챌린지를 생성한 유저입니다.");
                });

        Challenge challenge = new Challenge(requestDto, member);
        challengeRepository.save(challenge);
        challengeRecordRepository.save(new ChallengeRecord(challenge, member));
    }

    @Transactional
    public void putChallenge(PutChallengeRequestDto requestDto, String username) {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new NullPointerException("존재하지 않는 유저입니다."));
        Challenge challenge = challengeRepository.findByChallengeIdAndMember(requestDto.getChallengeId(), member)
                .orElseThrow(() -> new NullPointerException("존재하지 않는 챌린지입니다."));
        challenge.putChallenge(requestDto);
    }

    @Transactional
    public void deleteChallengeByAdmin(Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new NullPointerException("존재하지 않는 챌린지입니다."));
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
                .orElseThrow(() -> new NullPointerException("존재하지 않는 유저입니다."));

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

        Page<Challenge> pagedChallengeList = challengeRepository
                .findAllByCategoryNameOrderByModifiedAtDesc(categoryName, PageRequest.of(0, categorySize));

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

        for (Long challengeId : currentChallengeIdList) {
            if (!popularList.containsKey(challengeId)) {
                popularList.put(challengeId, 0);
            }
            recordList.stream().filter(record -> record.getChallenge().getChallengeId().equals(challengeId)).forEach(
                    record -> popularList.put(challengeId, popularList.get(challengeId) + 1));
        }

        List<Long> challengeIdList = new ArrayList<>();
        Collections.sort(challengeIdList, (value1, value2) -> (popularList.get(value2).compareTo(popularList.get(value1))));

        for (Long id : challengeIdList) {
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
                findAllByCategoryNameOrderByModifiedAtDesc(categoryName, PageRequest.of(page - 1, pageSize));
        return listResponseDtoSource(challengeList);
    }

    public ChallengeListResponseDto getChallengeSearchResult(String searchWords, int page) {
        final int pageSize = 6;

        Page<Challenge> challengeList = challengeRepository.
                findAllByChallengeTitleContaining(searchWords.trim(), PageRequest.of(page - 1, pageSize));
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
}
