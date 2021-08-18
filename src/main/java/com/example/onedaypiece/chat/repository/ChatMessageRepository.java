package com.example.onedaypiece.chat.repository;

import com.example.onedaypiece.chat.model.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Slice<ChatMessage> findAllByRoomIdOrderByCreatedAtDesc(String roomId, Pageable pageable);
}
