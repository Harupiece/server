package com.example.onedaypiece.chat.model;

import com.example.onedaypiece.chat.dto.request.ChatMessageRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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

    @Column
    private String createdAt; // 채팅 입력 시간

    @Builder
    public ChatMessage(MessageType type, String roomId, String sender, String message, String profileImg, String createdAt) {
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
        this.profileImg = profileImg;
        this.createdAt = createdAt;

    }

    public static ChatMessage createMessage(ChatMessageRequestDto requestDto) {
        return ChatMessage.builder()
                .type(requestDto.getType())
                .roomId(requestDto.getRoomId())
                .sender(requestDto.getNickname())
                .message(requestDto.getMessage())
                .profileImg(requestDto.getProfileImg())
                .createdAt(createTime())
                .build();
    }

    public void createENTER(String nickName){
        this.message = nickName + "님이 방에 입장했습니다.";
        this.sender = "[알림]";
    }

    public void createQUIT(String nickName){
        this.message = nickName + "님이 방에서 퇴장했습니다.";
        this.sender = "[알림]";
    }

    private static String createTime(){
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd E a HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        time.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        return time.format(date);
    }


}