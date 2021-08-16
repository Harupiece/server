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
    public String getRoomId(Object destination) {
        String destinationToString = String.valueOf(destination);

        int lastIndex = destinationToString.lastIndexOf('/');
        if (lastIndex != -1)
            return destinationToString.substring(lastIndex + 1);
        else
            return null;
    }

    // 채팅방에 알림 메세지 발송
    public void sendChatMessage(ChatMessageRequestDto requestDto) {

        System.out.println("requestDto = " + requestDto.getRoomId());
        System.out.println("requestDto = " + requestDto.getNickname());

        ChatMessage chatMessage = ChatMessage.createMessage(requestDto);
        Member member = getmember(requestDto);
        validateChatRoom(requestDto, member);

        if (ChatMessage.MessageType.ENTER.equals(requestDto.getType())) {
            chatMessage.createENTER(requestDto.getNickname());
            chatMessageRepository.save(chatMessage);

        }
        else if (ChatMessage.MessageType.QUIT.equals(requestDto.getType())) {
            chatMessage.createQUIT(requestDto.getNickname());
            chatMessageRepository.save(chatMessage);

        }
        else if(ChatMessage.MessageType.TALK.equals(requestDto.getType())){
            chatMessageRepository.save(chatMessage);
        }

        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
    }

    private Member getmember(ChatMessageRequestDto requestDto) {
        return memberRepository.findByNickname(requestDto.getNickname())
                .orElseThrow(() -> new ApiRequestException("조회되지 않는 회원입니다."));
    }

    private void validateChatRoom(ChatMessageRequestDto requestDto, Member member) {
        boolean b = challengeRecordRepository.existsByChallengeIdAndAndMember(
                Long.parseLong(requestDto.getRoomId()), member, 2L, 1L);

        log.info("챌린지 트루입니까? {}", b);

        if (!b) {
            throw new ApiRequestException("입장하실 챌린지가 없습니다.");
        }
    }
}