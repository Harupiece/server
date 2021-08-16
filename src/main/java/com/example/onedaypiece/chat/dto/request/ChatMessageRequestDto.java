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
    private String nickname; // sender
    private String alert; // [알림]
    private String profileImg; // 유저의 프로필 이미지
    private String message;
    private String createdAt;
}
