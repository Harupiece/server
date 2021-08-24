package com.example.onedaypiece.chat.service;

import com.example.onedaypiece.chat.dto.request.ChatMessageRequestDto;
import com.example.onedaypiece.chat.model.ChatMember;
import com.example.onedaypiece.chat.model.ChatMessage;
import com.example.onedaypiece.chat.repository.ChatMemberRepository;
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
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatMessageService {

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private final MemberRepository memberRepository;
    private final ChallengeRecordRepository challengeRecordRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMemberRepository chatMemberRepository;

    // tcp
    //destination정보에서 roomId 추출
    public String getRoomId(Object destination) {
        String destinationToString = String.valueOf(destination);

        int lastIndex = destinationToString.lastIndexOf('/');
        if (lastIndex != -1)
            return destinationToString.substring(lastIndex + 1);
        else
            return null;
    }

    // 채팅방에 메세지 발송
    @Transactional
    public void pubTalkMessage(ChatMessageRequestDto requestDto) {
        Member member = getMember(requestDto);
        validateChatRoom(requestDto, member);
        ChatMessage chatMessage = ChatMessage.createTALKMessage(requestDto);
        pubMessage(chatMessage);
    }

    @Transactional
    public void pubEnterMessage(ChatMessageRequestDto requestDto) {
        Member member = getMember(requestDto);
        validateChatRoom(requestDto, member);
        ChatMember chatMember = chatMemberRepository.findByMemberId(member);
        log.info("chatMember.getStatusFirst() 상태 가져오니: {}" ,chatMember.getStatusFirst());
        if(chatMember.getStatusFirst()) {
            ChatMessage chatMessage = ChatMessage.createENTERMessage(requestDto);
            log.info("chatMessage.getMessage() {}",requestDto.getMessage());
            log.info("requestDto.getType() {}",requestDto.getType());
            log.info("requestDto.getRoomId() {}",requestDto.getRoomId());
            log.info("requestDto.getAlert() {}",requestDto.getAlert());
            pubMessage(chatMessage);
            chatMember.setStatusFirstFalse();
        }
    }

    public void pubMessage(ChatMessage chatMessage){
        chatMessageRepository.save(chatMessage);
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
    }

    private Member getMember(ChatMessageRequestDto requestDto) {
        return memberRepository.findByNickname(requestDto.getNickname())
                .orElseThrow(() -> new ApiRequestException("조회되지 않는 회원입니다."));
    }

    private void validateChatRoom(ChatMessageRequestDto requestDto, Member member) {
        boolean b = challengeRecordRepository.existsByChallengeIdAndAndMember(
                Long.parseLong(requestDto.getRoomId()), member, 2L, 1L);
        if (!b) {
            throw new ApiRequestException("입장 가능한 챌린지가 없습니다.");
        }
    }
}