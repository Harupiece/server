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


@RequiredArgsConstructor
@RestController
//@RequestMapping("/api/chatroom")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    // 채팅방 생성
    // 챌린지와 함께 생성 redis 캐시에 저장

    // 채팅방 입장
    @GetMapping("/api/chatroom/{challengeId}")
    public ResponseEntity<ChatRoomResponseDto> getEachChatRoom(@PathVariable Long challengeId,
                                                               @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        return ResponseEntity.ok().body(chatRoomService.getEachChatRoom(challengeId, email));
    }
}

