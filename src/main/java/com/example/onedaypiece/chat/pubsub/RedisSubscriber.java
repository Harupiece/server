package com.example.onedaypiece.chat.pubsub;

import com.example.onedaypiece.chat.model.ChatMessage;
import com.example.onedaypiece.chat.repository.ChatMessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;


     // Redis에서 메시지가 발행(publish)되면 대기하고 있던 Redis Subscriber가 해당 메시지를 받아 처리한다.
    public void sendMessage(String publishMessage) {
        try {
            // ChatMessage 객채로 맵핑
            ChatMessage chatMessage = objectMapper.readValue(publishMessage, ChatMessage.class);
            // 채팅방을 구독한 클라이언트에게 메시지 발송
            messagingTemplate.convertAndSend("/sub/api/chat/rooms/" + chatMessage.getRoomId(), chatMessage);
            ChatMessage message = ChatMessage.builder()
                    .type(chatMessage.getType())
                    .roomId(chatMessage.getRoomId())
                    .sender(chatMessage.getSender())
                    .message(chatMessage.getMessage())
                    .profileImg(chatMessage.getProfileImg())
                    .build();
            chatMessageRepository.save(message);
            log.info("sub confirm {}, {}", chatMessage.getRoomId(), chatMessage);
        } catch (Exception e) {
            log.error("Subscriber Exception {}", e);
        }
    }
}