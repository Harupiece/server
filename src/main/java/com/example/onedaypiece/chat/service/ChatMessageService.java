package com.example.onedaypiece.chat.service;

import com.example.onedaypiece.chat.dto.request.ChatMessageRequestDto;
import com.example.onedaypiece.chat.model.ChatMessage;
import com.example.onedaypiece.chat.model.ChatRoom;
import com.example.onedaypiece.chat.repository.ChatMessageRepository;
import com.example.onedaypiece.chat.repository.ChatRoomRepository;
import com.example.onedaypiece.chat.repository.RedisRepository;
import com.example.onedaypiece.exception.ApiRequestException;
import com.example.onedaypiece.security.TokenProvider;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecordRepository;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatMessageService {

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private final MemberRepository memberRepository;
    private final ChallengeRecordRepository challengeRecordRepository;

    // tcp
    //destination정보에서 roomId 추출
    public String getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1)
            return destination.substring(lastIndex + 1);
        else
            return null;
    }

    // 채팅방에 알림 메세지 발송
    public void sendChatMessage(ChatMessage chatMessage) {
        Member member = memberRepository.findByNickname(chatMessage.getSender())
                .orElseThrow(()-> new ApiRequestException("조회되지 않는 회원입니다."));
        if (challengeRecordRepository.existsByChallengeIdAndAndMember(
                Long.parseLong(chatMessage.getRoomId()), member, 2L, 1L)) {
            if (ChatMessage.MessageType.ENTER.equals(chatMessage.getType())) {
                chatMessage.setMessage(chatMessage.getSender() + "님이 방에 입장했습니다.");
                chatMessage.setSender("[알림]");
            } else if (ChatMessage.MessageType.QUIT.equals(chatMessage.getType())) {
                chatMessage.setMessage(chatMessage.getSender() + "님이 방에서 나갔습니다.");
                chatMessage.setSender("[알림]");
            }
        }
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
    }


}
