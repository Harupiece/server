package com.example.onedaypiece.chat.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
public class ChatMember implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatMemberId;

    @Column
    private Long memberId;

    @Column
    private String roomId;

    @Column(nullable = false)
    private boolean statusFirst; // 채팅 첫방문인지 체크 true

    @Builder
    public ChatMember(Long memberId, String roomId, boolean statusFirst){
        this.memberId = memberId;
        this.roomId = roomId;
        this.statusFirst = statusFirst;
    }

    public static ChatMember createChatMember(Long memberId, String roomId){
        return ChatMember.builder()
                .memberId(memberId)
                .roomId(roomId)
                .statusFirst(true)
                .build();
    }

    public void setStatusFirstFalse(){
        this.statusFirst = false;
    }

}
