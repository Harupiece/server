package com.example.onedaypiece.chat.model;

import com.example.onedaypiece.chat.dto.request.ChatMessageRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class ChatMessage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatMessageId;

    public enum MessageType {
        // 메시지 타입 : 입장, 퇴장, 채팅
        ENTER, QUIT, TALK
    }

    @Column
    private MessageType type; // 메시지 타입

    @Column
    private String roomId; // 방번호 room

    @Column
    private String sender; // 메시지 보낸사람 nickname

    @Column
    private String message; // 메시지

    @Transient
    private Long memberCount; // 채팅방 인원수, 채팅방 내에서 메시지가 전달될때 인원수 갱신시 사용

    @Column
    private LocalDateTime createdAt;

    @Builder
    public ChatMessage(MessageType type, String roomId, String sender, String message, Long memberCount, LocalDateTime createdAt){
        this.type = type;
        this.roomId = roomId;
        this.sender = sender; // nickname
        this.message = message;
        this.memberCount = memberCount;
        this.createdAt = createdAt;
    }

    public ChatMessage(ChatMessageRequestDto requestDto, String nickName){
        this.type = requestDto.getType();
        this.roomId = requestDto.getRoomId();
        this.sender = nickName;
        this.message = requestDto.getMessage();
        this.memberCount = requestDto.getMemberCount();
        this.createdAt = requestDto.getCreatedAt();
    }
}

