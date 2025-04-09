package com.kkks.pofolling.chat.service;

import com.kkks.pofolling.chat.dto.ChatMessageResponseDTO;
import com.kkks.pofolling.chat.entity.ChatMessage;

import java.util.List;

public interface ChatMessageService {

    ChatMessageResponseDTO saveNewChatMessage(Long chatRoomId, Long senderId, String message);
    List<ChatMessageResponseDTO> findAllMessagesByChatRoomId(Long chatRoomId);

}
