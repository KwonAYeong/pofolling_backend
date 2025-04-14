package com.kkks.pofolling.chat.controller;

import com.kkks.pofolling.chat.dto.ChatMessageResponseDTO;
import com.kkks.pofolling.chat.dto.WebSocketMessageDTO;
import com.kkks.pofolling.chat.entity.ChatMessage;
import com.kkks.pofolling.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    // 프론트에서 /app/chat.send 호출하면 이 메서드로 옴
    @MessageMapping("/chat/message")
    public void sendMessage(@Payload WebSocketMessageDTO socketMessageDTO) {
        ChatMessageResponseDTO saved = chatMessageService.saveNewChatMessage(
                socketMessageDTO.getChatRoomId(),
                socketMessageDTO.getSenderId(),
                socketMessageDTO.getMessage());

        // 구독 중인 클라이언트에게 전송됨
        messagingTemplate.convertAndSend(
                "/topic/chatroom/" + socketMessageDTO.getChatRoomId(),
                saved
        );
    }
}
