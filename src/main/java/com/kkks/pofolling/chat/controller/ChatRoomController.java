package com.kkks.pofolling.chat.controller;

import com.kkks.pofolling.chat.dto.ChatMessageRequestDTO;
import com.kkks.pofolling.chat.dto.ChatRoomRequestDTO;
import com.kkks.pofolling.chat.service.ChatMessageService;
import com.kkks.pofolling.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping
    public ResponseEntity<?> createChatRoom(@RequestBody ChatRoomRequestDTO requestDTO) {
        return ResponseEntity.ok(
                chatRoomService.createChatRoom(requestDTO.getMentorId(), requestDTO.getPortfolioId())
        );
    }

    @GetMapping("/list/{userId}")
    public ResponseEntity<?> findChatRooms(@PathVariable Long userId) {
        return ResponseEntity.ok(chatRoomService.findAllChatRoomsByUserId(userId));
    }


}
