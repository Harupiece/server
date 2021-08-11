package com.example.onedaypiece.chat.service;

import com.example.onedaypiece.chat.dto.response.ChatRoomResponseDto;
import com.example.onedaypiece.exception.ApiRequestException;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecordRepository;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChallengeRecordRepository challengeRecordRepository;
    private final MemberRepository memberRepository;

    // 채팅방 생성 ChallengeService

    // 채팅방 입장(member가 현재 참여 중인)
    public ChatRoomResponseDto getEachChatRoom(Long challengeId, String email) {
        // 멤버인지 확인
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()->new ApiRequestException("이메일 정보가 확인되지 않습니다."));
        // 진행중인 챌린지인지 확인, 참여중인 멤버가 맞는지 확인
        ChallengeRecord challengeRecord = challengeRecordRepository.findtest(challengeId,member);

        //        System.out.println("chatRoomResponseDto. = " + chatRoomResponseDto.getRoomName());

        return new ChatRoomResponseDto(challengeRecord);
    }
}
