package com.example.onedaypiece.chat.model;

import com.example.onedaypiece.chat.dto.request.ChatMessageRequestDto;
import com.example.onedaypiece.service.MemberService;
import com.example.onedaypiece.web.domain.common.Timestamped;
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
public class ChatMessage  implements Serializable {

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
    private String roomId; // 방번호 chatroom id

    @Column
    private String sender; // 메시지 보낸사람 nickname, [알림]

    @Column
    private String message; // 메시지

    @Column
    private String profileImg; // 프로필 이미지

    @Builder
    public ChatMessage(MessageType type, String roomId, String sender, String message, String profileImg){
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
        this.profileImg = profileImg;

    }

    public ChatMessage(ChatMessageRequestDto requestDto){
        this.type = requestDto.getType();
        this.roomId = requestDto.getRoomId();
        this.sender = requestDto.getNickname();
        this.message = requestDto.getMessage();
        this.profileImg = requestDto.getProfileImg();
    }
}

