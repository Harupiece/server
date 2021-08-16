package com.example.onedaypiece.chat.dto.request;

import com.example.onedaypiece.chat.model.ChatMessage;
import lombok.Getter;

@Getter
public class ChatMessageRequestDto {

    private ChatMessage.MessageType type;
    private String roomId;
    private String nickname; // sender
    private String profileImg; // 유저의 프로필 이미지
    private String message;
    private String createdAt;

}
