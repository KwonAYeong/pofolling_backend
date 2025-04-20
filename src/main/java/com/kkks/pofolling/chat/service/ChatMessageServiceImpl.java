package com.kkks.pofolling.chat.service;

import com.kkks.pofolling.chat.dto.ChatMessageResponseDTO;
import com.kkks.pofolling.chat.entity.ChatMessage;
import com.kkks.pofolling.chat.entity.ChatRoom;
import com.kkks.pofolling.chat.repository.ChatMessageRepository;
import com.kkks.pofolling.chat.repository.ChatRoomRepository;
import com.kkks.pofolling.exception.BusinessException;
import com.kkks.pofolling.exception.ExceptionCode;
import com.kkks.pofolling.user.entity.User;
import com.kkks.pofolling.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    // 채팅 메세지 저장
    // 📁 ChatMessageServiceImpl.java

    @Override
    @Transactional
    public ChatMessageResponseDTO saveNewChatMessage(Long chatRoomId, Long senderId, String message) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.CHATROOM_NOT_FOUND));

        if (!chatRoom.isActive()) {
            throw new BusinessException(ExceptionCode.CHATROOM_CLOSED);  // 커스텀 예외 코드 추가 필요
        }

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        ChatMessage saved = chatMessageRepository.save(ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .message(message)
                .sentAt(LocalDateTime.now())
                .build());

        return ChatMessageResponseDTO.builder()
                .messageId(saved.getMessageId())
                .chatRoomId(chatRoomId)
                .senderId(senderId)
                .senderNickname(sender.getNickname())
                .senderProfileImage(sender.getProfileImage())
                .message(saved.getMessage())
                .sentAt(saved.getSentAt())
                .build();
    }


    // 채팅방의 모든 메세지 조회
    @Override
    public List<ChatMessageResponseDTO> findAllMessagesByChatRoomId(Long chatRoomId) {
        List<ChatMessage> messages = chatMessageRepository.findByChatRoom_ChatRoomIdOrderBySentAt(chatRoomId);

        return messages.stream().map(msg ->
                ChatMessageResponseDTO.builder()
                        .messageId(msg.getMessageId())
                        .chatRoomId(msg.getChatRoom().getChatRoomId())
                        .senderId(msg.getSender().getUserId())
                        .senderNickname(msg.getSender().getNickname())
                        .senderProfileImage(msg.getSender().getProfileImage())
                        .message(msg.getMessage())
                        .sentAt(msg.getSentAt())
                        .build()
        ).toList();
    }
}
