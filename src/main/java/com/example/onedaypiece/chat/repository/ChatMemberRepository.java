package com.example.onedaypiece.chat.repository;

import com.example.onedaypiece.chat.model.ChatMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {
    ChatMember findByMemberIdAndRoomId(Long memberId, String roomId);
}
