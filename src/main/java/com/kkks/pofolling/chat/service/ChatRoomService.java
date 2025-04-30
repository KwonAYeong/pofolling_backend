package com.kkks.pofolling.chat.service;

import com.kkks.pofolling.chat.dto.ChatRoomResponseDTO;
import com.kkks.pofolling.chat.entity.ChatRoom;

import java.util.List;

public interface ChatRoomService {

    ChatRoomResponseDTO createChatRoom(Long mentorId, Long portfolioId);
    ChatRoom createChatRoomIfNotExists(Long mentorId, Long menteeId, Long portfolioId);
    List<ChatRoomResponseDTO> findAllChatRoomsByUserId(Long userId);
    void deactivateChatRoom(Long chatRoomId);
}
