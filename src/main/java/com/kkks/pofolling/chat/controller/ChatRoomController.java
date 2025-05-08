package com.kkks.pofolling.chat.controller;

import com.kkks.pofolling.chat.dto.ChatMessageRequestDTO;
import com.kkks.pofolling.chat.dto.ChatRoomRequestDTO;
import com.kkks.pofolling.chat.service.ChatMessageService;
import com.kkks.pofolling.chat.service.ChatRoomService;
import com.kkks.pofolling.chat.service.ChatRoomServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatRoomServiceImpl chatRoomServiceImpl;

    // 채팅방 생성
    @PostMapping
    public ResponseEntity<?> createChatRoom(@RequestBody ChatRoomRequestDTO requestDTO) {
        return ResponseEntity.ok(
                chatRoomService.createChatRoom(requestDTO.getMentorId(), requestDTO.getPortfolioId())
        );
    }

    // 채팅방 목록 조회
    @GetMapping("/list/{userId}")
    public ResponseEntity<?> findChatRooms(@PathVariable Long userId) {
        return ResponseEntity.ok(chatRoomService.findAllChatRoomsByUserId(userId));
    }

    // 채팅방 종료
    @PatchMapping("/{chatRoomId}/deactivate")
    public ResponseEntity<String> deactivateChatRoom (@PathVariable Long chatRoomId) {
        chatRoomService.deactivateChatRoom(chatRoomId);
        return ResponseEntity.ok("첨삭종료");
    }

}
