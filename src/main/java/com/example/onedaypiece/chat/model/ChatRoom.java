package com.example.onedaypiece.chat.model;

import com.example.onedaypiece.web.domain.challenge.Challenge;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Entity
@NoArgsConstructor
//@RedisHash("chatRoom")
public class ChatRoom implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient
    private Long memberCount; // 현재 대화 참여 중인 인원

    @Column
    private String roomId; // challengeId

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge; // roomId

    @Builder
    public ChatRoom(Challenge challenge) {
        this.challenge = challenge;
        this.roomId = String.valueOf(challenge.getChallengeId());
    }
}
