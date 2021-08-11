package com.example.onedaypiece.chat.service;

import com.example.onedaypiece.chat.dto.request.ChatMessageRequestDto;
import com.example.onedaypiece.chat.model.ChatMessage;
import com.example.onedaypiece.chat.repository.ChatMessageRepository;
import com.example.onedaypiece.chat.repository.RedisRepository;
import com.example.onedaypiece.exception.ApiRequestException;
import com.example.onedaypiece.security.TokenProvider;
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
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final RedisRepository redisRepository;
    private final ChatMessageRepository chatMessageRepository;

    // 채팅방 대화 불러오기
    public List<ChatMessage> getMessage(String roomId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findAllByRoomIdOrderByCreatedAt(roomId);
        // 파일
        return chatMessages;
    }

    // message publish - redis
    public void pubMessage(ChatMessageRequestDto requestDto, String email) {
        // 로그인 토큰 확인
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiRequestException("일치하는 회원 정보가 없습니다.")
                );

        // 로그인 회원 정보로 대화명 설정
        String nickname = member.getNickname();
        log.info("nickname sender : " + nickname);
        requestDto.setSender(nickname);

        // 채팅방 인원수 설정
        log.info("MemberCount : " + redisRepository.getMemberCount(requestDto.getRoomId()));
        requestDto.setMemberCount(redisRepository.getMemberCount(requestDto.getRoomId()));

        // 메세지 생성 시간 설정
        LocalDateTime current = LocalDateTime.now();
        log.info("createdAt : "+ current);
        requestDto.setCreatedAt(current);

        ChatMessage message = new ChatMessage(requestDto, nickname);

        // Websocket에 발행된 메시지를 redis로 발행(publish)
        sendChatMessage(message);
        chatMessageRepository.save(message);
    }

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
        chatMessage.setMemberCount(redisRepository.getMemberCount(chatMessage.getRoomId()));
        if (ChatMessage.MessageType.ENTER.equals(chatMessage.getType())) {
            chatMessage.setMessage(chatMessage.getSender() + "님이 방에 입장했습니다.");
            chatMessage.setSender("[알림]");
        } else if (ChatMessage.MessageType.QUIT.equals(chatMessage.getType())) {
            chatMessage.setMessage(chatMessage.getSender() + "님이 방에서 나갔습니다.");
            chatMessage.setSender("[알림]");
        }
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
    }


}
