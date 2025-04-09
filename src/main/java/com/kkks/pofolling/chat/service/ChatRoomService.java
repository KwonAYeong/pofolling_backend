package com.kkks.pofolling.chat.service;

import com.kkks.pofolling.chat.dto.ChatRoomResponseDTO;
import com.kkks.pofolling.chat.entity.ChatRoom;

import java.util.List;

public interface ChatRoomService {
    ChatRoomResponseDTO createChatRoom(Long mentorId, Long portfolioId);
    List<ChatRoomResponseDTO> findAllChatRoomsByUserId(Long userId);
}
