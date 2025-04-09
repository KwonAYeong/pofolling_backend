package com.kkks.pofolling.chat.controller;

import com.kkks.pofolling.chat.dto.ChatMessageRequestDTO;
import com.kkks.pofolling.chat.dto.ChatMessageResponseDTO;
import com.kkks.pofolling.chat.entity.ChatMessage;
import com.kkks.pofolling.chat.service.ChatMessageService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat/{chatRoomId}")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @PostMapping("/messages")
    public ResponseEntity<ChatMessageResponseDTO> sendMessage(
            @PathVariable Long chatRoomId,
            @RequestBody ChatMessageRequestDTO requestDTO) {

        ChatMessageResponseDTO response = chatMessageService.saveNewChatMessage(
                chatRoomId,
                requestDTO.getSenderId(),
                requestDTO.getMessage());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/messages")
    public ResponseEntity<?> getMessages(@PathVariable Long chatRoomId) {
        return ResponseEntity.ok(chatMessageService.findAllMessagesByChatRoomId(chatRoomId));
    }

}
