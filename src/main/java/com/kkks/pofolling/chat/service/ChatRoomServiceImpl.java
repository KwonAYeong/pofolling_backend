package com.kkks.pofolling.chat.service;

import com.kkks.pofolling.chat.dto.ChatRoomResponseDTO;
import com.kkks.pofolling.chat.entity.ChatMessage;
import com.kkks.pofolling.chat.entity.ChatRoom;
import com.kkks.pofolling.chat.repository.ChatMessageRepository;
import com.kkks.pofolling.chat.repository.ChatRoomRepository;
import com.kkks.pofolling.exception.BusinessException;
import com.kkks.pofolling.exception.ExceptionCode;
import com.kkks.pofolling.mypage.entity.Portfolio;
import com.kkks.pofolling.mypage.entity.PortfolioStatus;
import com.kkks.pofolling.mypage.repository.PortfolioRepository;
import com.kkks.pofolling.user.entity.User;
import com.kkks.pofolling.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;

    // 포트폴리오 수락 시 채팅방 생성 또는 기존 채팅방에 연결
    @Override
    @Transactional
    public ChatRoomResponseDTO createChatRoom(Long mentorId, Long portfolioId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.PORTFOLIO_NOT_FOUND));

        if (portfolio.getUser() == null) {
            throw new BusinessException(ExceptionCode.USER_NOT_FOUND);
        }

        if (portfolio.getStatus() != PortfolioStatus.REQUESTED) {
            throw new BusinessException(ExceptionCode.INVALID_PORTFOLIO_STATUS);
        }

        User mentor = userRepository.findById(mentorId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.MENTOR_NOT_FOUND));
        User mentee = portfolio.getUser();

        // 기존 활성 채팅방 있는지 확인
        ChatRoom chatRoom = chatRoomRepository
                .findByMentor_UserIdAndMentee_UserIdAndIsActiveTrue(mentorId, mentee.getUserId())
                .stream()
                .findFirst()
                .orElseGet(() -> {
                    ChatRoom newRoom = ChatRoom.builder()
                            .mentor(mentor)
                            .mentee(mentee)
                            .isActive(true)
                            .build();
                    return chatRoomRepository.save(newRoom);
                });

        // 포트폴리오에 채팅방 연결
        portfolio.setChatRoom(chatRoom);
        portfolio.updateStatus(PortfolioStatus.IN_PROGRESS);

        return convertToDTO(chatRoom);
    }

    // 멘토-멘티 기반 채팅방 조회 또는 생성 (포트폴리오 연결 X)
    @Override
    @Transactional
    public ChatRoom createChatRoomIfNotExists(Long mentorId, Long menteeId) {
        return chatRoomRepository
                .findByMentor_UserIdAndMentee_UserIdAndIsActiveTrue(mentorId, menteeId)
                .stream()
                .findFirst()
                .orElseGet(() -> {
                    User mentor = userRepository.findById(mentorId)
                            .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
                    User mentee = userRepository.findById(menteeId)
                            .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

                    ChatRoom newRoom = ChatRoom.builder()
                            .mentor(mentor)
                            .mentee(mentee)
                            .isActive(true)
                            .build();

                    return chatRoomRepository.save(newRoom);
                });
    }

    // 채팅방 목록 조회
    @Override
    public List<ChatRoomResponseDTO> findAllChatRoomsByUserId(Long userId) {
        List<ChatRoom> rooms = chatRoomRepository.findByMentor_UserIdOrMentee_UserId(userId, userId);

        return rooms.stream()
                .map(room -> {
                    Optional<ChatMessage> lastMessageOpt = chatMessageRepository
                            .findTopByChatRoomOrderBySentAtDesc(room);
                    String lastMessageContent = lastMessageOpt.map(ChatMessage::getMessage).orElse("");

                    boolean hasNewMessage = lastMessageOpt.isPresent();

                    User opponent = room.getMentor().getUserId().equals(userId)
                            ? room.getMentee()
                            : room.getMentor();

                    List<Long> portfolioIds = portfolioRepository.findAllByChatRoom(room).stream()
                            .map(Portfolio::getPortfolioId)
                            .collect(Collectors.toList());

                    return ChatRoomResponseDTO.builder()
                            .chatRoomId(room.getChatRoomId())
                            .portfolioIds(portfolioIds)
                            .mentorId(room.getMentor().getUserId())
                            .menteeId(room.getMentee().getUserId())
                            .senderId(opponent.getUserId())
                            .senderNickname(opponent.getNickname())
                            .senderProfileImage(opponent.getProfileImage())
                            .lastMessage(lastMessageContent)
                            .isActive(room.isActive())
                            .hasNewMessage(hasNewMessage)
                            .createdAt(room.getCreatedAt())
                            .updatedAt(room.getUpdatedAt())
                            .build();
                })
                .collect(Collectors.toList());
    }

    // 채팅방 종료 → 모든 연결된 포트폴리오 상태 COMPLETED 처리
    @Transactional
    public void deactivateChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.CHATROOM_NOT_FOUND));

        chatRoom.deactivate();

        List<Portfolio> portfolios = portfolioRepository.findAllByChatRoom(chatRoom);
        portfolios.forEach(p -> p.updateStatus(PortfolioStatus.COMPLETED));
    }

    // 공통 DTO 변환
    private ChatRoomResponseDTO convertToDTO(ChatRoom room) {
        List<Long> portfolioIds = portfolioRepository.findAllByChatRoom(room).stream()
                .map(Portfolio::getPortfolioId)
                .collect(Collectors.toList());

        return ChatRoomResponseDTO.builder()
                .chatRoomId(room.getChatRoomId())
                .portfolioIds(portfolioIds)
                .mentorId(room.getMentor().getUserId())
                .menteeId(room.getMentee().getUserId())
                .createdAt(room.getCreatedAt())
                .updatedAt(room.getUpdatedAt())
                .build();
    }
}
