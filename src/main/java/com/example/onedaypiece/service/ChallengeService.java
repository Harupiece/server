package com.example.onedaypiece.service;

import com.example.onedaypiece.chat.model.ChatMember;
import com.example.onedaypiece.chat.model.ChatRoom;
import com.example.onedaypiece.chat.repository.ChatMemberRepository;
import com.example.onedaypiece.chat.repository.ChatRoomRepository;
import com.example.onedaypiece.exception.ApiRequestException;
import com.example.onedaypiece.web.domain.challenge.CategoryName;
import com.example.onedaypiece.web.domain.challenge.Challenge;
import com.example.onedaypiece.web.domain.challenge.ChallengeQueryRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.onedaypiece.web.domain.challenge.CategoryName.*;
import static com.example.onedaypiece.web.domain.challenge.Challenge.createChallenge;
import static com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord.createChallengeRecord;
import static com.example.onedaypiece.web.dto.response.challenge.ChallengeDetailResponseDto.createChallengeDetailResponseDto;
import static com.example.onedaypiece.web.dto.response.challenge.ChallengeMainResponseDto.createChallengeMainResponseDto;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeQueryRepository challengeQueryRepository;
    private final ChallengeRecordRepository challengeRecordRepository;
    private final ChallengeRecordQueryRepository challengeRecordQueryRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMemberRepository chatMemberRepository;

    // 채팅룸 저장
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, ChatRoom> hashOpsChatRoom;
    private static final String CHAT_ROOMS = "CHAT_ROOM";

    private final LocalDateTime currentLocalDateTime = LocalDateTime.now();

    public ChallengeDetailResponseDto getChallengeDetail(Long challengeId) {
        Challenge challenge = challengeChecker(challengeId);
        List<ChallengeDetailResponseDtoMemberDto> memberList = challengeRecordQueryRepository.findAllByChallengeId(challengeId);
        return createChallengeDetailResponseDto(challenge, memberList);
    }

    @Transactional
    public void deleteChallenge(Long challengeId, String username) {
        Challenge challenge = challengeChecker(challengeId);
        deleteChallengeException(username, challenge);

        List<ChallengeRecord> recordList = challengeRecordRepository.findAllByChallengeAndChallengeRecordStatusTrue(challenge);
        recordList.forEach(ChallengeRecord::setStatusFalse);
    }

    @Transactional
    public Long postChallenge(ChallengeRequestDto requestDto, String email) {
        Member member = memberChecker(email);
        createChallengeException(requestDto, member);

        System.out.println("email = " + email);
        System.out.println("member.getMemberId() = " + member.getMemberId());

        Challenge challenge = createChallenge(requestDto, member);
        challengeRepository.save(challenge);

        Long challengeId = challenge.getChallengeId();
        ChallengeRecord challengeRecord = createChallengeRecord(challenge, member);
        challengeRecordRepository.save(challengeRecord);

        ChatRoom chatRoom = new ChatRoom(challengeId);
        chatRoomRepository.save(chatRoom);
        hashOpsChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);

        ChatMember chatMember = ChatMember.createChatMember(member.getMemberId(), chatRoom.getRoomId());
        chatMemberRepository.save(chatMember);

        return challengeId;
    }

    @Transactional
    public void putChallenge(PutChallengeRequestDto requestDto, String email) {
        Member member = memberChecker(email);
        Challenge challenge = challengeChecker(requestDto.getChallengeId());
        putChallengeException(member, challenge);
        challenge.putChallenge(requestDto);
    }

    public ChallengeMainResponseDto getMainPage() {
        ChallengeMainResponseDto responseDto = createChallengeMainResponseDto();
        List<ChallengeRecord> records = challengeRecordQueryRepository.findAllByStatusTrue();

        sliderUpdate(responseDto, records);
        popularUpdate(responseDto, records);

        categoryCollector(EXERCISE, records).forEach(responseDto::addExercise);
        categoryCollector(LIVINGHABITS, records).forEach(responseDto::addLivingHabits);
        categoryCollector(NODRINKNOSMOKE, records).forEach(responseDto::addNoDrinkNoSmoke);

        return responseDto;
    }

    private void sliderUpdate(ChallengeMainResponseDto responseDto, List<ChallengeRecord> records) {
        List<Challenge> officialList = challengeQueryRepository.findAllByOfficialChallenge();

        List<ChallengeSourceResponseDto> sliderSourceList = officialList
                .stream()
                .map(challenge -> new ChallengeSourceResponseDto(challenge, records))
                .collect(Collectors.toList());

        responseDto.addSlider(sliderSourceList);
    }

    private void popularUpdate(ChallengeMainResponseDto responseDto, List<ChallengeRecord> records) {
        final int POPULAR_SIZE = 4;

        List<ChallengeRecord> popularRecords = challengeRecordQueryRepository
                .findAllPopular(PageRequest.of(0, POPULAR_SIZE));
        responseDto.addPopular(popularRecords, records);
    }

    private List<ChallengeSourceResponseDto> categoryCollector(CategoryName category, List<ChallengeRecord> records) {
        final int CATEGORY_SIZE = 3;

        List<Challenge> challenges = records
                .stream()
                .filter(r -> r.getChallenge().getCategoryName().equals(category))
                .map(ChallengeRecord::getChallenge)
                .distinct()
                .limit(CATEGORY_SIZE)
                .collect(Collectors.toList());

        return challenges
                .stream()
                .map(c -> new ChallengeSourceResponseDto(c, records))
                .collect(Collectors.toList());
    }

    private Challenge challengeChecker(Long challengeId) {
        return challengeQueryRepository.findById(challengeId)
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
            throw new ApiRequestException("작성자가 아닙니다.");
        }
        if (currentLocalDateTime.isBefore(challenge.getChallengeStartDate())) {
            challenge.setChallengeStatusFalse();
            challenge.updateChallengeProgress(3L);
        } else {
            throw new ApiRequestException("이미 시작된 챌린지는 삭제할 수 없습니다.");
        }
    }

    private void createChallengeException(ChallengeRequestDto requestDto, Member member) {
        List<ChallengeRecord> recordList = challengeRecordQueryRepository.findAllByMember(member);
        if (recordList.size() >= 10) {
            throw new ApiRequestException("이미 10개의 챌린지에 참가하고 있는 유저입니다.");
        }
        if (requestDto.getChallengePassword().length() < 4) {
            if (!requestDto.getChallengePassword().equals("")) {
                throw new ApiRequestException("비밀번호는 4자리 이상으로 설정해야합니다.");
            }
        }
        if (!StringUtils.hasText(requestDto.getChallengeTitle())) {
            throw new ApiRequestException("제목이 없습니다.");
        }
        if (!StringUtils.hasText(requestDto.getChallengeContent())) {
            throw new ApiRequestException("내용이 없습니다.");
        }
        if (!StringUtils.hasText(requestDto.getChallengeImgUrl())) {
            throw new ApiRequestException("챌린지 이미지가 없습니다.");
        }
        if (!StringUtils.hasText(requestDto.getChallengeGood())) {
            throw new ApiRequestException("좋은 예시가 없습니다.");
        }
        if (!StringUtils.hasText(requestDto.getChallengeBad())) {
            throw new ApiRequestException("나쁜 예시가 없습니다.");
        }
    }
}
