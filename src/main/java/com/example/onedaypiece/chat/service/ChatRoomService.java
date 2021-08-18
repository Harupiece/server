package com.example.onedaypiece.chat.service;

import com.example.onedaypiece.chat.dto.response.ChatMessageResponseDto;
import com.example.onedaypiece.chat.dto.response.ChatRoomResponseDto;
import com.example.onedaypiece.chat.model.ChatMessage;
import com.example.onedaypiece.chat.repository.ChatMessageRepository;
import com.example.onedaypiece.exception.ApiRequestException;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecordRepository;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Slf4j
public class ChatRoomService {

    private final ChallengeRecordRepository challengeRecordRepository;
    private final MemberRepository memberRepository;
    private final ChatMessageRepository chatMessageRepository;

    // 채팅방 생성

    // 채팅방 입장(member가 현재 참여 중인)
    public ChatRoomResponseDto getEachChatRoom(String roomId, String email,int page) {

        Member member = getMember(email);
        Long challengeId = Long.parseLong(roomId);
        existsByChallengeProgress(member, challengeId);


        Pageable pageable = PageRequest.of(page-1,15);
        Slice<ChatMessage> chatMessages = chatMessageRepository.findAllByRoomIdOrderByCreatedAtDesc(roomId,pageable);
        List<ChatMessageResponseDto> chatMessageResponseDtoList = chatMessages.getContent().stream()
                .sorted(Comparator.comparing(chatMessage -> chatMessage.getCreatedAt()))
                .map(chatMessage -> new ChatMessageResponseDto(chatMessage))
                .collect(Collectors.toList());

        return ChatRoomResponseDto.builder()
                .chatMessages(chatMessageResponseDtoList)
                .hasNext(chatMessages.hasNext())
                .build();
    }

    private Member getMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiRequestException("이메일 정보가 확인되지 않습니다."));
    }

    private void existsByChallengeProgress(Member member, Long challengeId) {
        if (!challengeRecordRepository.existsByChallengeIdAndAndMember(challengeId, member, 2L, 1L)){
            throw new ApiRequestException("종료된 챌린지에서의 채팅 서비스를 사용할 수 없습니다.");
        }
    }
}
