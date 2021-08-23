package com.example.onedaypiece.chat.controller;

import com.example.onedaypiece.chat.dto.response.ChatRoomResponseDto;
import com.example.onedaypiece.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    // 채팅방 생성
    // 챌린지와 함께 생성 redis 캐시에 저장

    // 채팅방 입장
    // 채팅방 대화 불러오기
    @GetMapping("/chat/messages/{roomId}")
    public ChatRoomResponseDto getEachChatRoom(@PathVariable String roomId,
                                                               @RequestParam("page") int page,
                                                               @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        return chatRoomService.getEachChatRoom(roomId, email,page);
    }

    // 채팅방 삭제
}

