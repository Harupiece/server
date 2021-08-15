package com.example.onedaypiece.chat.service;

import com.example.onedaypiece.chat.dto.response.ChatRoomResponseDto;
import com.example.onedaypiece.chat.model.ChatMessage;
import com.example.onedaypiece.chat.repository.ChatMessageRepository;
import com.example.onedaypiece.chat.repository.ChatRoomRepository;
import com.example.onedaypiece.exception.ApiRequestException;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecordRepository;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChallengeRecordRepository challengeRecordRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    // 채팅방 생성

    // 채팅방 입장(member가 현재 참여 중인)
    public ChatRoomResponseDto getEachChatRoom(String roomId, String email) {
        // 멤버인지 확인
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()->new ApiRequestException("이메일 정보가 확인되지 않습니다."));
        // 진행예정이거나 진행중인 챌린지인지 확인, 참여중인 멤버가 맞는지 확인
        Long challengeId = Long.parseLong(roomId);
        if (challengeRecordRepository.existsByChallengeIdAndAndMember(challengeId, member, 2L, 1L)){
            List<ChatMessage> chatMessages = chatMessageRepository.findAllByRoomIdOrderByCreatedAt(roomId);
            return new ChatRoomResponseDto(roomId, chatMessages);
        } else {
            throw new ApiRequestException("종료된 챌린지에서의 채팅 서비스를 사용할 수 없습니다.");
        }
    }
}
