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
                .findByMentor_UserIdAndMentee_UserId(mentorId, menteeId)  // 모든 채팅방 조회 (isActive 제거)
                .stream()
                .findFirst()
                .map(chatRoom -> {
                    if (!chatRoom.isActive()) {  // 비활성화 상태면
                        chatRoom.activate();    // 다시 활성화 (isActive = true)
                        chatRoomRepository.save(chatRoom);
                    }
                    return chatRoom;
                })
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

                    // 현재 사용자 기준으로 메세지 내용과 새 메세지 여부 처리
                    String lastMessageContent = lastMessageOpt
                            .map(msg -> msg.getSender().getUserId().equals(userId)
                                    ? msg.getMessage()
                                    : "새 메세지가 도착했습니다.")
                            .orElse("");

                    boolean hasNewMessage = lastMessageOpt
                            .map(msg -> !msg.getSender().getUserId().equals(userId))
                            .orElse(false);

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
                            .hasNewMessage(hasNewMessage)
                            .isActive(room.isActive())
                            .createdAt(room.getCreatedAt())
                            .updatedAt(room.getUpdatedAt())
                            .build();
                })
                .collect(Collectors.toList());
    }

    // 채팅방 종료 → 모든 연결된 포트폴리오 상태 COMPLETED 처리
    @Transactional
    public ChatRoomResponseDTO deactivateChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.CHATROOM_NOT_FOUND));

        chatRoom.deactivate();
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        // (IN_PROGRESS 상태인 포트폴리오만 필터링
        List<Portfolio> inProgressPortfolios = portfolioRepository.findAllByChatRoom(chatRoom).stream()
                .filter(p -> p.getStatus() == PortfolioStatus.IN_PROGRESS)
                .collect(Collectors.toList());

        // 필터링된 포트폴리오만 상태 변경
        inProgressPortfolios.forEach(p -> p.updateStatus(PortfolioStatus.COMPLETED));

        // 필터링된 포트폴리오만 카운트 증가
        inProgressPortfolios.forEach(Portfolio::increaseEditCount);

        // 최근 메시지 조회
        Optional<ChatMessage> lastMessageOpt = chatMessageRepository.findTopByChatRoomOrderBySentAtDesc(savedChatRoom);
        User sender = lastMessageOpt.map(ChatMessage::getSender).orElse(savedChatRoom.getMentee());
        String lastMessageText = lastMessageOpt.map(ChatMessage::getMessage).orElse(null);

        // 최신 상태 반환
        return new ChatRoomResponseDTO(
                savedChatRoom.getChatRoomId(),
                savedChatRoom.getPortfolios().stream()
                        .map(Portfolio::getPortfolioId)
                        .collect(Collectors.toList()),
                savedChatRoom.getMentor().getUserId(),
                savedChatRoom.getMentee().getUserId(),
                sender.getUserId(),
                sender.getNickname(),
                sender.getProfileImage(),
                lastMessageText,
                false,
                savedChatRoom.isActive(),
                savedChatRoom.getCreatedAt(),
                savedChatRoom.getUpdatedAt()
        );
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