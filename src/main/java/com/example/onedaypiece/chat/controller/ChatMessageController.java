package com.example.onedaypiece.chat.controller;

import com.example.onedaypiece.chat.dto.request.ChatMessageRequestDto;
import com.example.onedaypiece.chat.model.ChatMessage;
import com.example.onedaypiece.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

     // websocket "/pub/chat/message"로 들어오는 메시징을 처리한다. publish

    // 채팅방 대화 불러오기
    @GetMapping("/message/{roomId}")
    public ResponseEntity<List<ChatMessage>> getMessage(@PathVariable String roomId){
        return ResponseEntity.ok().body(chatMessageService.getMessage(roomId));
    }

    // 웹소켓으로 들어오는 메시지 발행 처리 -> 클라이언트에서는 /pub/chat/message로 발행 요청
    @MessageMapping("/message")
    public ResponseEntity<Void> pubMessage(@RequestBody ChatMessageRequestDto requestDto,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        chatMessageService.pubMessage(requestDto, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
