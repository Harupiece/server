package com.example.onedaypiece.service;

import com.example.onedaypiece.chat.model.ChatRoom;
import com.example.onedaypiece.chat.repository.ChatRoomRepository;
import com.example.onedaypiece.exception.ApiRequestException;
import com.example.onedaypiece.web.domain.challenge.CategoryName;
import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.challenge.ChallengeRepository;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecordQueryRepository;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecordRepository;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.member.MemberRepository;
import com.example.onedaypiece.web.dto.request.challenge.ChallengeRequestDto;
import com.example.onedaypiece.web.dto.request.challenge.PutChallengeRequestDto;
import com.example.onedaypiece.web.dto.response.challenge.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.onedaypiece.web.domain.challenge.CategoryName.*;
import static java.lang.Math.min;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeRecordRepository challengeRecordRepository;
    private final ChallengeRecordQueryRepository challengeRecordQueryRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;

    // 채팅룸 저장
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, ChatRoom> hashOpsChatRoom;
    private static final String CHAT_ROOMS = "CHAT_ROOM";

    private final LocalDateTime currentLocalDateTime = LocalDateTime.now();

    public ChallengeResponseDto getChallengeDetail(Long challengeId) {
        Challenge challenge = ChallengeChecker(challengeId);
        List<Long> memberList = challengeRecordQueryRepository.findAllByChallengeId(challengeId)
                .stream()
                .map(c -> c.getMember().getMemberId())
                .collect(Collectors.toList());
        return new ChallengeResponseDto(challenge, memberList);
    }

    @Transactional
    public void deleteChallenge(Long challengeId, String username) {
        Challenge challenge = ChallengeChecker(challengeId);
        deleteChallengeException(username, challenge);

        ChallengeRecord record = challengeRecordRepository.findByChallengeAndChallengeRecordStatusTrue(challenge);
        record.setStatusFalse();
    }

    @Transactional
    public Long createChallenge(ChallengeRequestDto requestDto, String email) {
        Member member = memberChecker(email);
        createChallengeException(requestDto, member);

        Challenge challenge = new Challenge(requestDto, member);
        ChallengeRecord challengeRecord = new ChallengeRecord(challenge, member);
        challengeRecordRepository.save(challengeRecord);
        Long challengeId = challengeRepository.save(challenge).getChallengeId();

        ChatRoom chatRoom = new ChatRoom(challengeId);
        chatRoomRepository.save(chatRoom);
        hashOpsChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);

        return challengeId;
    }

    @Transactional
    public void putChallenge(PutChallengeRequestDto requestDto, String email) {
        Member member = memberChecker(email);
        Challenge challenge = ChallengeChecker(requestDto.getChallengeId());
        putChallengeException(member, challenge);
        challenge.putChallenge(requestDto);
    }

    public List<ChallengeSourceResponseDto> getAllChallenge(int page) {
        List<ChallengeRecord> records = challengeRecordQueryRepository.findAllByChallengeStatusTrueAndProgressNotStart();
        List<Challenge> challenges = records.stream().map(ChallengeRecord::getChallenge).collect(Collectors.toList());
//        List<Long> challengeIdList = new ArrayList<>();
//        List<ChallengeSourceResponseDto> sources = challenges
//                .stream()
//                .filter(c -> !challengeIdList.contains(c.getChallengeId()))
//                .peek(c -> challengeIdList.add(c.getChallengeId()))
//                .map(c -> new ChallengeSourceResponseDto(c, records))
//                .collect(Collectors.toList());

       return challenges
                .stream()
                .distinct()
                .map(c -> new ChallengeSourceResponseDto(c, records))
                .collect(Collectors.toList());
//        return listToPage(page, sources);
    }

    public ChallengeMainResponseDto getMainPage(String email) {
        ChallengeMainResponseDto responseDto = new ChallengeMainResponseDto();
        List<ChallengeRecord> records = challengeRecordQueryRepository.findAllByStatusTrue();

        sliderUpdate(responseDto, email, records);
        popularUpdate(responseDto, email, records);

        categoryCollector(EXERCISE, records).forEach(responseDto::addExercise);
        categoryCollector(LIVINGHABITS, records).forEach(responseDto::addLivingHabits);
        categoryCollector(NODRINKNOSMOKE, records).forEach(responseDto::addNoDrinkNoSmoke);

//        responseDto.setHistoryCount(userHistoryRepository.countAllByStatusFalseAndMemberEmail(email));
        return responseDto;
    }

    private void sliderUpdate(ChallengeMainResponseDto responseDto, String email, List<ChallengeRecord> records) {
        List<Challenge> userChallengeList = records
                .stream()
                .filter(r -> r.getChallenge().getCategoryName().equals(OFFICIAL))
                .map(ChallengeRecord::getChallenge)
                .collect(Collectors.toList());
        List<ChallengeSourceResponseDto> sliderSourceList = userChallengeList
                .stream()
                .map(challenge -> new ChallengeSourceResponseDto(challenge, records))
                .collect(Collectors.toList());
        responseDto.addSlider(sliderSourceList);
    }

    private void popularUpdate(ChallengeMainResponseDto responseDto, String email, List<ChallengeRecord> records) {
        final int POPULAR_SIZE = 4;
        List<ChallengeRecord> popularRecords = challengeRecordQueryRepository.findPopular(email, PageRequest.of(0, POPULAR_SIZE));
        responseDto.addPopular(popularRecords, records);
    }

    private List<ChallengeSourceResponseDto> categoryCollector(CategoryName category, List<ChallengeRecord> records) {
        final int CATEGORY_SIZE = 3;

//        Set<Long> recordIdList = new HashSet<>();
//        List<ChallengeRecord> recordList = new ArrayList<>();
//
//        records
//                .stream()
//                .filter(r -> !recordIdList.contains(r.getChallenge().getChallengeId()) &&
//                        r.getChallenge().getCategoryName().equals(category) &&
//                        recordIdList.size() < CATEGORY_SIZE).forEach(r -> {
//            recordIdList.add(r.getChallenge().getChallengeId());
//            recordList.add(r);
//        });

        List<ChallengeRecord> recordList = records.stream()
                .filter(r -> r.getChallenge().getCategoryName().equals(category))
                .distinct()
                .limit(CATEGORY_SIZE)
                .collect(Collectors.toList());

        List<Challenge> list = recordList.stream().map(ChallengeRecord::getChallenge).collect(Collectors.toList());
        return list
                .stream()
                .map(c -> new ChallengeSourceResponseDto(c, records))
                .collect(Collectors.toList());
    }

    private Page<ChallengeSourceResponseDto> listToPage(int page, List<ChallengeSourceResponseDto> sources) {
        final int allChallengePageSize = 8;
        Pageable paging = PageRequest.of(page - 1, allChallengePageSize);
        final int start = min((int) paging.getOffset(), sources.size());
        final int end = min((start + paging.getPageSize()), sources.size());

        return new PageImpl<>(sources.subList(start, end), paging, sources.size());
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
            challenge.setChallengeStatusFalse();
            challenge.updateChallengeProgress(3L);
        } else {
            throw new ApiRequestException("이미 시작된 챌린지는 삭제할 수 없습니다.");
        }
    }

    private void createChallengeException(ChallengeRequestDto requestDto, Member member) {
        List<ChallengeRecord> recordList = challengeRecordRepository.findAllByMember(member);
        if (recordList.stream().anyMatch(r -> r.getChallenge().getCategoryName().equals(requestDto.getCategoryName()))) {
            throw new ApiRequestException("이미 해당 카테고리에 챌린지를 생성한 유저입니다.");
        }
        if (requestDto.getChallengePassword().length() < 4) {
            if (!requestDto.getChallengePassword().equals("")) {
                throw new ApiRequestException("비밀번호는 4자리 이상으로 설정해야합니다.");
            }
        }
    }
}
