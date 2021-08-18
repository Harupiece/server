package com.example.onedaypiece.chat.dto.response;

import com.example.onedaypiece.chat.model.ChatMessage;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@NoArgsConstructor
public class ChatRoomResponseDto {

    private Long count; // 인원수
    private List<ChatMessageResponseDto> chatMessages;
    private boolean hasNext;

    @Builder
    public ChatRoomResponseDto(List<ChatMessageResponseDto> chatMessages, boolean hasNext) {
        this.chatMessages = chatMessages;
        this.hasNext = hasNext;
    }
}

