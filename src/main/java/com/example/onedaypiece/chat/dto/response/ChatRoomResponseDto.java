package com.example.onedaypiece.chat.dto.response;

import com.example.onedaypiece.web.domain.challengeRecord.ChallengeRecord;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Getter
@NoArgsConstructor
public class ChatRoomResponseDto {

    private String roomId;


    public ChatRoomResponseDto(ChallengeRecord challenge){
        this.roomId = String.valueOf(challenge.getChallenge().getChallengeId());

    }
}
