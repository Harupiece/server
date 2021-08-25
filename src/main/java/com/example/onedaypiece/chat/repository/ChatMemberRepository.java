package com.example.onedaypiece.chat.repository;

import com.example.onedaypiece.chat.model.ChatMember;
import com.example.onedaypiece.web.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {
    ChatMember findByMemberIdAndRoomId(Long memberId, String roomId);
}
