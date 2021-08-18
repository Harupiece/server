package com.example.onedaypiece.chat.model;

import com.example.onedaypiece.chat.dto.request.ChatMessageRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Entity
@NoArgsConstructor
public class ChatMessage  implements Serializable {

    private static final String BAD_WORD = "badword";

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
                .message(messageFilter(requestDto.getMessage()))
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

    public static String createTime(){
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd E a HH:mm");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        time.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        return time.format(date);
    }

    //createBadwordList().contains(message)
    public static String messageFilter(String message){
////        List<String>
//
//
////        List<Integer> indexList = new ArrayList<>();
////        int index = createBadwordList().indexOf(message);
////        while(index != -1){
////            // 걸린 욕설 찾아와야 함
////            indexList.add(index);
////            index = createBadwordList().indexOf(message, createBadwordList().get());
////            String targetWord = message;
////            message = message.replace(message, "**");
//        }
        return message;
    }

    // /home/dhkdrb897/badwordList.txt
    public static List<String> createBadwordList(){
        Path path = Paths.get("C:/Users/User/Desktop/bad.txt");
        List<String> list = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                list = Arrays.asList(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}