package com.example.onedaypiece.chat.dto.response;

import com.example.onedaypiece.chat.model.ChatMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMessageResponseDto {

    private ChatMessage.MessageType type;
    private String roomId;
    private String sender;
    private String message;
    private String profileImg;
    private String createdAt;

    public ChatMessageResponseDto(ChatMessage chatMessage){
        this.type = chatMessage.getType();
        this.roomId = chatMessage.getRoomId();
        this.sender = chatMessage.getSender();
        this.message = chatMessage.getMessage();
        this.profileImg = chatMessage.getProfileImg();
        this.createdAt = chatMessage.getCreatedAt();
    }
}
