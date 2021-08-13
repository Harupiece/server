package com.example.onedaypiece.chat.dto.response;

import com.example.onedaypiece.chat.model.ChatMessage;
import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
public class ChatRoomResponseDto {

    private Long roomId;
    private Long count; // 인원수
    private List<ChatMessage> chatMessages;

    public ChatRoomResponseDto(Long roomId, List<ChatMessage> chatMessages){
        this.roomId = roomId;
        this.chatMessages = chatMessages;
    }
}
