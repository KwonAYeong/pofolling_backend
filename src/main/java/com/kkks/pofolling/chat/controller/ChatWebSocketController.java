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

    // WebSocket 기반 실시간 채팅 메세지 처리 메서드
    // 프론트에서 "/app/chat/message" 주소로 WebSocket 메세지를 보내면 실행되는 메서드
    @MessageMapping("/chat/message")
    public void sendMessage(@Payload WebSocketMessageDTO socketMessageDTO) {

        // 메세지를 DB에 저장하고 DTO로 반환
        ChatMessageResponseDTO saved = chatMessageService.saveNewChatMessage(
                socketMessageDTO.getChatRoomId(),
                socketMessageDTO.getSenderId(),
                socketMessageDTO.getMessage());

        // 해당 채팅방을 구독 중인 모든 클라이언트에게 메세지 전송
        messagingTemplate.convertAndSend(
                "/topic/chatroom/" + socketMessageDTO.getChatRoomId(),
                saved
        );
    }
}
