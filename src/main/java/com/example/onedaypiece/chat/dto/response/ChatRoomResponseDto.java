package com.example.onedaypiece.chat.dto.response;

import com.example.onedaypiece.chat.model.ChatMessage;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@NoArgsConstructor
public class ChatRoomResponseDto {

    private String roomId;
    private Long count; // 인원수
    private List<ChatMessage> chatMessages;
    private boolean hasNext;

    @Builder
    public ChatRoomResponseDto(String roomId, List<ChatMessage> chatMessages,boolean hasNext){
        this.roomId = roomId;
        this.chatMessages = chatMessages;
        this.hasNext=hasNext;
    }
}
