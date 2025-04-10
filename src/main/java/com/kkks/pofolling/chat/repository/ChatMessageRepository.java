package com.kkks.pofolling.chat.repository;

import com.kkks.pofolling.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByChatRoom_ChatRoomIdOrderBySentAt(Long chatRoomId);
}
