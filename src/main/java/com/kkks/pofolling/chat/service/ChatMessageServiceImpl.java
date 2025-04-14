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

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ChatMessageResponseDTO saveNewChatMessage(Long chatRoomId, Long senderId, String message) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.CHATROOM_NOT_FOUND));

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        ChatMessage saved = chatMessageRepository.save(ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .message(message)
                .build());

        return ChatMessageResponseDTO.builder()
                .messageId(saved.getMessageId())
                .chatRoomId(chatRoomId)
                .senderId(senderId)
                .message(saved.getMessage())
                .sentAt(saved.getSentAt())
                .build();
    }

    @Override
    public List<ChatMessageResponseDTO> findAllMessagesByChatRoomId(Long chatRoomId) {
        List<ChatMessage> messages = chatMessageRepository.findByChatRoom_ChatRoomIdOrderBySentAt(chatRoomId);

        return messages.stream().map(msg ->
                ChatMessageResponseDTO.builder()
                        .messageId(msg.getMessageId())
                        .chatRoomId(msg.getChatRoom().getChatRoomId())
                        .senderId(msg.getSender().getUserId())
                        .message(msg.getMessage())
                        .sentAt(msg.getSentAt())
                        .build()
        ).toList();
    }
}
