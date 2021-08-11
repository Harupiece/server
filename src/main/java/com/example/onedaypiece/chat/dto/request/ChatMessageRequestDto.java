package com.example.onedaypiece.chat.dto.request;

import com.example.onedaypiece.chat.model.ChatMessage;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessageRequestDto {

    private ChatMessage.MessageType type;
    private String roomId;
    private String sender;
    private String message;
    private LocalDateTime createdAt;
    private Long memberCount;
}
