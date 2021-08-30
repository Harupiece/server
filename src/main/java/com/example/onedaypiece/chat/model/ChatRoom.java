package com.example.onedaypiece.chat.model;

import com.example.onedaypiece.web.domain.common.Timestamped;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Entity
@NoArgsConstructor
//@RedisHash("chatRoom")
public class ChatRoom extends Timestamped implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String roomId;


    @Builder
    public ChatRoom(String roomId){
        this.roomId = roomId;
    }

    /*
        채팅방 생성
     */
    public static ChatRoom createChatRoom(Long challengeId){
        return ChatRoom.builder()
                .roomId(String.valueOf(challengeId))
                .build();
    }
}
