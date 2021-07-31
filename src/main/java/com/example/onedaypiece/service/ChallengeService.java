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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
        Challenge challenge = ChallengeChecker(challengeId);
        return new ChallengeResponseDto(challenge,
                challengeRecordRepository.findAllByChallengeId(challengeId)
                        .stream()
                        .map(c -> c.getMember().getMemberId())
                        .collect(Collectors.toList()));
    }

    @Transactional
    public void deleteChallenge(Long challengeId, String username) {
        Challenge challenge = ChallengeChecker(challengeId);
        deleteChallengeException(username, challenge);
        challengeRecordRepository.deleteAllByChallenge(challenge); // 챌린지 참가 기록 삭제
    }

    @Transactional
    public Long createChallenge(ChallengeRequestDto requestDto, String email) {
        Member member = memberChecker(email);
        createChallengeException(requestDto, member);
        Challenge challenge = new Challenge(requestDto, member);
        challengeRecordRepository.save(new ChallengeRecord(challenge, member));
        return challengeRepository.save(challenge).getChallengeId();
    }

    @Transactional
    public void putChallenge(PutChallengeRequestDto requestDto, String email) {
        Member member = memberChecker(email);
        Challenge challenge = ChallengeChecker(requestDto.getChallengeId());
        putChallengeException(member, challenge);

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

        // 유저가 참가한 챌린지 추가
        List<ChallengeRecord> allByMember = challengeRecordRepository.findAllByMemberEmail(userEmail);
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

    private Challenge ChallengeChecker(Long challengeId) {
        return challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ApiRequestException("존재하지 않는 챌린지입니다"));
    }

    private void putChallengeException(Member member, Challenge challenge) {
        if (!challenge.getMember().equals(member)) {
            throw new ApiRequestException("다른 유저가 만든 챌린지입니다.");
        }
        if (!challenge.getChallengeProgress().equals(1L)) {
            throw new ApiRequestException("이미 시작되거나 종료된 챌린지입니다.");
        }
        if (!challenge.isChallengeStatus()) {
            throw new ApiRequestException("삭제된 챌린지입니다.");
        }
    }

    private Member memberChecker(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiRequestException("존재하지 않는 유저입니다."));
    }

    private void deleteChallengeException(String username, Challenge challenge) {
        if (!challenge.getMember().getEmail().equals(username)) {
            throw new IllegalArgumentException("작성자가 아닙니다.");
        }
        if (currentLocalDateTime.isBefore(challenge.getChallengeStartDate())) {
            challenge.setChallengeStatus(false);
            challenge.setChallengeProgress(3L);
        } else {
            throw new ApiRequestException("이미 시작된 챌린지는 삭제할 수 없습니다.");
        }
    }

    private void createChallengeException(ChallengeRequestDto requestDto, Member member) {
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
    }
}
