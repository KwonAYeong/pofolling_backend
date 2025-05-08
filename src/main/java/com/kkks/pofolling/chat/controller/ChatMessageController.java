package com.kkks.pofolling.chat.controller;

import com.kkks.pofolling.chat.dto.ChatMessageListResponseDTO;
import com.kkks.pofolling.chat.dto.ChatMessageRequestDTO;
import com.kkks.pofolling.chat.dto.ChatMessageResponseDTO;
import com.kkks.pofolling.chat.entity.ChatMessage;
import com.kkks.pofolling.chat.entity.ChatRoom;
import com.kkks.pofolling.chat.repository.ChatRoomRepository;
import com.kkks.pofolling.chat.service.ChatMessageService;
import com.kkks.pofolling.exception.BusinessException;
import com.kkks.pofolling.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat/{chatRoomId}")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final ChatRoomRepository chatRoomRepository;

    // 채팅 메세지 전송
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

    // 채팅방의 모든 메세지 조회 (채팅방 종료 여부 포함)
    @GetMapping("/messages")
    public ResponseEntity<ChatMessageListResponseDTO> getMessages(@PathVariable Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.CHATROOM_NOT_FOUND));

        boolean isActive = chatRoom.isActive();
        var messages = chatMessageService.findAllMessagesByChatRoomId(chatRoomId);

        ChatMessageListResponseDTO response = ChatMessageListResponseDTO.builder()
                .isActive(isActive)
                .messages(messages)
                .build();

        return ResponseEntity.ok(response);
    }

}
