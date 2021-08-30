package com.example.onedaypiece.chat.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ChatRoomResponseDto {

    private List<ChatMessageResponseDto> chatMessages;
    private boolean hasNext;

    @Builder
    public ChatRoomResponseDto(List<ChatMessageResponseDto> chatMessages, boolean hasNext) {
        this.chatMessages = chatMessages;
        this.hasNext = hasNext;
    }
}

