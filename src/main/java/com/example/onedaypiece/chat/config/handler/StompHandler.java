package com.example.onedaypiece.chat.config.handler;

import com.example.onedaypiece.chat.model.ChatMessage;
import com.example.onedaypiece.chat.repository.RedisRepository;
import com.example.onedaypiece.chat.service.ChatMessageService;
import com.example.onedaypiece.security.TokenProvider;
import com.example.onedaypiece.web.domain.member.Member;
import com.example.onedaypiece.web.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    private final TokenProvider tokenProvider;
    private final ChatMessageService chatMessageService;
    private final RedisRepository redisRepository;
    private final MemberRepository memberRepository;

    private Authentication authentication;

    // websocket을 통해 들어온 요청이 처리 되기전 실행된다.
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // websocket 연결요청
        if (StompCommand.CONNECT == accessor.getCommand()) {
            String jwtToken = accessor.getFirstNativeHeader("token");
            log.info("CONNECT {}", jwtToken);
            tokenProvider.validateToken(jwtToken);

        // 채팅룸 구독요청
        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
            // header정보에서 구독 destination정보를 얻고, roomId를 추출한다.
            // roomId를 URL로 전송해주고 있어 추출 필요
            String roomId = chatMessageService.getRoomId(Optional.ofNullable((String) message.getHeaders().get("simpDestination")).orElse("Invalid RoomId"));

            // 채팅방에 들어온 클라이언트 sessionId를 roomId와 맵핑해 놓는다.(나중에 특정 세션이 어떤 채팅방에 들어가 있는지 알기 위함)
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            redisRepository.setMemberEnterInfo(sessionId, roomId);

            // subscribe 의 token 확인
            String jwtToken = accessor.getFirstNativeHeader("token");
            log.info("SUBSCRIBE {}", jwtToken);

            // 클라이언트 입장 메시지를 채팅방에 발송한다.(redis publish)
            Member member = memberRepository.findByEmail(tokenProvider.getMemberEmail(jwtToken))
                    .orElseThrow(() -> new RuntimeException("등록되지 않은 회원입니다."));
            String nickname = member.getNickname();
            chatMessageService.sendChatMessage(ChatMessage.builder()
                    .type(ChatMessage.MessageType.ENTER)
                    .roomId(roomId)
                    .sender(nickname)
                    .build());
            log.info("SUBSCRIBED {}, {}", nickname, roomId);

            // Websocket 연결 종료
        } else if (StompCommand.DISCONNECT == accessor.getCommand()) {
            // 연결이 종료된 클라이언트 sesssionId로 채팅방 id를 얻는다.
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            String roomId = redisRepository.getMemberEnterRoomId(sessionId);

            // 클라이언트 퇴장 메시지를 채팅방에 발송한다.(redis publish)
            String email = Optional.ofNullable((Principal) message.getHeaders().get("simpUser")).map(Principal::getName).orElse("UnknownUser");
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(()->new RuntimeException("등록되지 않은 회원입니다."));
            String nickname = member.getNickname();
            chatMessageService.sendChatMessage(ChatMessage.builder().type(ChatMessage.MessageType.QUIT).roomId(roomId).sender(nickname).build());

            // 퇴장한 클라이언트의 roomId 맵핑 정보를 삭제한다.
            redisRepository.removeMemberEnterInfo(sessionId);
            log.info("DISCONNECTED {}, {}", sessionId, roomId);
        }
        return message;
    }
}
