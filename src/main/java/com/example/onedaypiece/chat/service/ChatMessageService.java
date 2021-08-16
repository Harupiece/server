package com.example.onedaypiece.chat.service;

import com.example.onedaypiece.chat.dto.request.ChatMessageRequestDto;
import com.example.onedaypiece.chat.model.ChatMessage;
import com.example.onedaypiece.chat.repository.ChatMessageRepository;
import com.example.onedaypiece.exception.ApiRequestException;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecordRepository;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatMessageService {

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private final MemberRepository memberRepository;
    private final ChallengeRecordRepository challengeRecordRepository;
    private final ChatMessageRepository chatMessageRepository;

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
    public void sendChatMessage(ChatMessageRequestDto requestDto) {

        ChatMessage chatMessage = new ChatMessage(requestDto);
        Member member = getmember(requestDto);
        validateChatRoom(requestDto, member);

        if (ChatMessage.MessageType.ENTER.equals(requestDto.getType())) {
            chatMessage.createENTER(requestDto);
            chatMessageRepository.save(chatMessage);

        } else if (ChatMessage.MessageType.QUIT.equals(requestDto.getType())) {
            chatMessage.createQUIT(requestDto);
            chatMessageRepository.save(chatMessage);

        }else if(ChatMessage.MessageType.TALK.equals(requestDto.getType())){
            chatMessageRepository.save(chatMessage);
        }

        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
    }

    private Member getmember(ChatMessageRequestDto requestDto) {
        return memberRepository.findByNickname(requestDto.getNickname())
                .orElseThrow(() -> new ApiRequestException("조회되지 않는 회원입니다."));
    }

    private void validateChatRoom(ChatMessageRequestDto requestDto, Member member) {
        if (!challengeRecordRepository.existsByChallengeIdAndAndMember(
                Long.parseLong(requestDto.getRoomId()), member, 2L, 1L)) {
            throw new ApiRequestException("입장하실 챌린지가 없습니다.");
        }
    }
}