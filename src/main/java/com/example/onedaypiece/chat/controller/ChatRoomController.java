package com.example.onedaypiece.chat.controller;

import com.example.onedaypiece.chat.dto.response.ChatRoomResponseDto;
import com.example.onedaypiece.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    // 채팅방 생성
    // 챌린지와 함께 생성 redis 캐시에 저장

    // 채팅방 입장
    // 채팅방 대화 불러오기
    @GetMapping("/chat/messages/{roomId}")
    public ResponseEntity<ChatRoomResponseDto> getEachChatRoom(@PathVariable Long roomId,
                                                                    @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        return ResponseEntity.ok().body(chatRoomService.getEachChatRoom(roomId, email));
    }

    // 채팅방 삭제
}

