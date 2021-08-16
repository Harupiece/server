package com.example.onedaypiece.chat.repository;

import com.example.onedaypiece.chat.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

}
