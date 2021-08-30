package com.example.onedaypiece.chat.model;

import com.example.onedaypiece.chat.dto.request.ChatMessageRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.cfg.annotations.reflection.XMLContext;


import javax.persistence.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private String roomId; // 방번호 chatroom id

    @Column
    private String sender; // 메시지 보낸사람 nickname, [알림]

    @Column
    private String message; // 메시지

    @Column
    private String profileImg; // 프로필 이미지

    @Column
    private String createdAt; // 채팅 입력 시간

    @Column
    private String alert; // [알림]

    @Builder
    public ChatMessage(MessageType type, String roomId, String sender, String message, String profileImg, String createdAt, String alert) {
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
        this.profileImg = profileImg;
        this.createdAt = createdAt;
        this.alert = alert;
    }

    /*
       type이 talk인 메세지 생성
    */
    public static ChatMessage createTALKMessage(ChatMessageRequestDto requestDto) {
        return ChatMessage.builder()
                .type(requestDto.getType())
                .roomId(requestDto.getRoomId())
                .sender(requestDto.getNickname())
                .message(messageFilter(requestDto.getMessage()))
                .profileImg(requestDto.getProfileImg())
                .createdAt(createTime())
                .alert(requestDto.getAlert())
                .build();
    }

    /*
      type이 enter인 메세지 생성
    */
    public static ChatMessage createENTERMessage(ChatMessageRequestDto requestDto) {
        return ChatMessage.builder()
                .type(requestDto.getType())
                .roomId(requestDto.getRoomId())
                .sender(requestDto.getNickname())
                .message(requestDto.getNickname() + "님이 챌린지에 참여했습니다.")
                .profileImg(requestDto.getProfileImg())
                .createdAt(createTime())
                .alert(requestDto.getAlert())
                .build();
    }

    /*
      메세지 생성 시간 표기
    */
    public static String createTime() {
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd E a HH:mm");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        time.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        return time.format(date);
    }

    // /home/dhkdrb897/badword.txt
    // C:/Users/User/Desktop/bad.txt
    /*
      욕설 필터
    */
    public static String messageFilter(String message) {
        FileInputStream fis;
        InputStreamReader isr;
        BufferedReader bReader;
        try {
            // 욕설을 저장해둔 txt 파일을 읽어 온다.
            fis = new FileInputStream("/home/dhkdrb897/badword.txt");
            isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            bReader = new BufferedReader(isr);
            String words = bReader.readLine();
            // 배열로 저장
            String[] badwordList = words.split(",");
            int size = badwordList.length;
            String filterword = "";
            // 포문을 돌려 필터할 단어를 저장
            for (int i = 0; i < size; i++) {
                filterword = badwordList[i];
                // 빈칸 제거
                filterword = filterword.trim();
                // 작성자가 보낸 메세지에 필터할 단어가 있는지 확인
                if (message.contains(filterword)) {
                    int length = filterword.length();
                    String toChange = "";
                    int j = 0;
                    // 필터된 단어 길이만큼 *로 반환
                    while (j < length) {
                        toChange = toChange + "*";
                        j++;
                    }
                    // 메세지 내용을 *로 변환
                    message = message.replaceAll(filterword, toChange);
                    System.out.println(message);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("지정 경로에 해당파일 없음");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }
}