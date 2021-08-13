package com.example.onedaypiece.chat.repository;

import com.example.onedaypiece.chat.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByRoomId(String roomId);

//    @Query("select c ,c.challenge.member " +
//            "from ChatRoom c " +
//            "join fetch c.challenge " +
//            "where c.member.email = :email")
//    Optional<ChatRoom> findByMemberEmail(String email);

}
