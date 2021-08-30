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

    /*
        tcp
        destination정보에서 roomId 추출
     */
    public String getRoomId(Object destination) {
        String destinationToString = String.valueOf(destination);

        int lastIndex = destinationToString.lastIndexOf('/');
        if (lastIndex != -1)
            return destinationToString.substring(lastIndex + 1);
        else
            return null;
    }

    /*
        채팅방에 메세지 발송
     */
    /*
        type이 talk일 때의 메서드
     */
    @Transactional
    public void pubTalkMessage(ChatMessageRequestDto requestDto) {
        Member member = getMember(requestDto);
        validateChatRoom(requestDto, member);
        ChatMessage chatMessage = ChatMessage.createTALKMessage(requestDto);
        pubMessage(chatMessage);
    }

    /*
       type이 enter일 때의 메서드
     */
    @Transactional
    public void pubEnterMessage(ChatMessageRequestDto requestDto) {
        Member member = getMember(requestDto);
        validateChatRoom(requestDto, member);
        // 채팅방 최초 접근자임을 확인
        ChatMember chatMember = chatMemberRepository.findByMemberIdAndRoomId(member.getMemberId(), requestDto.getRoomId());
        // 최초 접근자의 경우 알림 메세지 발송
        if(chatMember.isStatusFirst()) {
            ChatMessage chatMessage = ChatMessage.createENTERMessage(requestDto);
            pubMessage(chatMessage);
            chatMember.setStatusFirstFalse();
        }
    }

    /*
        sub 하고 있는 이용자에게 메세지 pub
     */
    public void pubMessage(ChatMessage chatMessage){
        chatMessageRepository.save(chatMessage);
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
    }

    /*
        회원 조회
     */
    private Member getMember(ChatMessageRequestDto requestDto) {
        return memberRepository.findByNickname(requestDto.getNickname())
                .orElseThrow(() -> new ApiRequestException("조회되지 않는 회원입니다."));
    }

    /*
       챌린지가 진행 중 또는 진행 예정일 경우와 챌린지를 참여한 경우에만 채팅방 접근 가능
     */
    private void validateChatRoom(ChatMessageRequestDto requestDto, Member member) {
        boolean validate = challengeRecordRepository.existsByChallengeIdAndAndMember(
                Long.parseLong(requestDto.getRoomId()), member, 2L, 1L);
        if (!validate) {
            throw new ApiRequestException("입장 가능한 챌린지가 없습니다.");
        }
    }
}