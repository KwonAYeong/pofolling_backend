package com.kkks.pofolling.chat.repository;

import com.kkks.pofolling.chat.entity.ChatMessage;
import com.kkks.pofolling.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByChatRoom_ChatRoomIdOrderBySentAt(Long chatRoomId);
    Optional<ChatMessage> findTopByChatRoomOrderBySentAtDesc(ChatRoom chatRoom);
    Optional<ChatMessage> findTopByChatRoom_ChatRoomIdOrderBySentAtDesc(Long chatRoomId);


}
