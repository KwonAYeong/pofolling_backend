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

import java.util.Comparator;
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

    // 채팅방 생성 요청
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

        Optional<ChatRoom> existingRoom = chatRoomRepository.findByPortfolio(portfolio);
        if (existingRoom.isPresent()) {
            return convertToDTO(existingRoom.get());
        }

        ChatRoom chatRoom = ChatRoom.builder()
                .portfolio(portfolio)
                .mentor(mentor)
                .mentee(portfolio.getUser())
                .build();

        ChatRoom saved = chatRoomRepository.save(chatRoom);
        return convertToDTO(saved);
    }

    // 채팅방 생성
    @Override
    @Transactional
    public ChatRoom createChatRoomIfNotExists(Long mentorId, Long menteeId, Long portfolioId) {
        Optional<ChatRoom> existingRoom = chatRoomRepository
                .findByMentor_UserIdAndMentee_UserIdAndPortfolio_PortfolioId(mentorId, menteeId, portfolioId);

        if (existingRoom.isPresent()) {
            ChatRoom room = existingRoom.get();

            // 비활성화 상태면 다시 활성화
            if (!room.isActive()) {
                room.activate(); // 또는 room.setIsActive(true);
            }

            return room;
        }

        // 기존 방 없음 → 새로 생성
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.PORTFOLIO_NOT_FOUND));
        User mentor = userRepository.findById(mentorId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        User mentee = userRepository.findById(menteeId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        ChatRoom newRoom = ChatRoom.builder()
                .portfolio(portfolio)
                .mentor(mentor)
                .mentee(mentee)
                .isActive(true)
                .build();

        return chatRoomRepository.save(newRoom);
    }


    // 채팅방 목록 조회
    @Override
    public List<ChatRoomResponseDTO> findAllChatRoomsByUserId(Long userId) {
        List<ChatRoom> rooms = chatRoomRepository.findByMentor_UserIdOrMentee_UserId(userId, userId);

        return rooms.stream()
                .map(room -> {
                    // ① 마지막 메시지 하나만 조회
                    Optional<ChatMessage> lastMessageOpt = chatMessageRepository
                            .findTopByChatRoom_ChatRoomIdOrderBySentAtDesc(room.getChatRoomId());

                    String lastMessageContent = lastMessageOpt.map(ChatMessage::getMessage)
                            .orElse("");

                    // ② 상대방 정보
                    User opponent = room.getMentor().getUserId().equals(userId)
                            ? room.getMentee()
                            : room.getMentor();

                    // ③ 포트폴리오 리스트
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
                            .lastMessage(lastMessageContent) // ✅ 항상 표시
                            .isActive(room.isActive())
                            .createdAt(room.getCreatedAt())
                            .updatedAt(room.getUpdatedAt())
                            .build();
                })
                .collect(Collectors.toList());
    }


    // 채팅방 종료
    @Transactional
    public void deactivateChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.CHATROOM_NOT_FOUND));

        chatRoom.deactivate();

        Portfolio portfolio = chatRoom.getPortfolio();
        portfolio.updateStatus(PortfolioStatus.COMPLETED);
    }

    // 공통 DTO 변환
    private ChatRoomResponseDTO convertToDTO(ChatRoom room) {
        return ChatRoomResponseDTO.builder()
                .chatRoomId(room.getChatRoomId())
                .portfolioIds(List.of(room.getPortfolio().getPortfolioId()))
                .mentorId(room.getMentor().getUserId())
                .menteeId(room.getMentee().getUserId())
                .createdAt(room.getCreatedAt())
                .updatedAt(room.getUpdatedAt())
                .build();
    }
}
