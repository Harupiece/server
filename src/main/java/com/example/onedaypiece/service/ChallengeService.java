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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

//        challenge = challengeProgressChecker(challenge);

        List<Long> challengeMember = new ArrayList<>();
        challengeRecordRepository.findAllByChallengeId(challengeId).forEach(
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
//        challenge = challengeProgressChecker(challenge);
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

    // Guest 메인 페이지
    public ChallengeGuestMainResponseDto getGuestMainChallengeDetail() {
        ChallengeGuestMainResponseDto mainRequestDto = new ChallengeGuestMainResponseDto();

        sliderListUpdate(mainRequestDto, "");

        categoryCollector(EXERCISE).forEach(mainRequestDto::addExercise);
        categoryCollector(LIVINGHABITS).forEach(mainRequestDto::addLivingHabits);
        categoryCollector(NODRINKNOSMOKE).forEach(mainRequestDto::addNoDrinkNoSmoke);
        return mainRequestDto;
    }

    // Member 메인 페이지
    public ChallengeMemberMainResponseDto getMemberMainChallengeDetail(String userEmail) {
        ChallengeMemberMainResponseDto mainResponseDto = new ChallengeMemberMainResponseDto();

        List<ChallengeRecord> allByMember = challengeRecordRepository.findAllByMemberEmail(userEmail);

        // 유저가 참가한 챌린지 추가
        mainResponseDto.addSlider(allByMember
                .stream()
                .map(o -> (new ChallengeSliderSourceResponseDto(o.getChallenge(), allByMember)))
                .collect(Collectors.toList()));

        sliderListUpdate(mainResponseDto, userEmail);

        categoryCollector(EXERCISE).forEach(mainResponseDto::addExercise);
        categoryCollector(LIVINGHABITS).forEach(mainResponseDto::addLivingHabits);
        categoryCollector(NODRINKNOSMOKE).forEach(mainResponseDto::addNoDrinkNoSmoke);
        return mainResponseDto;
    }

    private List<ChallengeSliderSourceResponseDto> categoryCollector(CategoryName categoryName) {

        final int categorySize = 3;

        List<Challenge> challengeList = challengeRepository
                .findAllByCategoryNameOrderByModifiedAtDescListed(categoryName, PageRequest.of(0, categorySize));

        List<ChallengeSliderSourceResponseDto> returnList;

        List<ChallengeRecord> allByMember = challengeRecordRepository.findAllByChallengeList(challengeList);
        returnList = challengeList
                .stream()
                .map(challenge -> new ChallengeSliderSourceResponseDto(challenge, allByMember))
                .collect(Collectors.toList());

        return returnList;
    }

    private void sliderListUpdate(ChallengeGuestMainResponseDto mainResponseDto, String email) {
        List<ChallengeRecord> recordList = challengeRecordRepository
                .findAllStatusTrueAndProgressNotStartedYet(email, PageRequest.of(0, 5 - mainResponseDto.getSlider().size()));

        List<ChallengeSliderSourceResponseDto> sliderList = recordList
                .stream()
                .map(record -> (new ChallengeSliderSourceResponseDto(record.getChallenge(), recordList)))
                .collect(Collectors.toList());

        mainResponseDto.addSlider(sliderList);
    }

    public List<Challenge> getAllChallenge() {
        return challengeRepository.findAll();
    }

//    @Transactional
//    Challenge challengeProgressChecker(Challenge challenge) {
//        if (currentLocalDateTime.isBefore(challenge.getChallengeStartDate())) {
//            challenge.setChallengeProgress(1L);
//        } else if (currentLocalDateTime.isBefore(challenge.getChallengeEndDate())) {
//            challenge.setChallengeProgress(2L);
//        } else {
//            challenge.setChallengeProgress(3L);
//            challengeRecordRepository.findAllByChallengeId(challenge.getChallengeId())
//                    .forEach(ChallengeRecord::setChallengeRecordStatusToFalse);
//        }
//
//        return challenge;
//    }
}
