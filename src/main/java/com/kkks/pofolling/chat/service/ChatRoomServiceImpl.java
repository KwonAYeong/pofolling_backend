package com.kkks.pofolling.chat.service;

import com.kkks.pofolling.chat.dto.ChatRoomResponseDTO;
import com.kkks.pofolling.chat.entity.ChatRoom;
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

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;

    // 채팅방 생성
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
            ChatRoom room = existingRoom.get();
            return convertToDTO(room);
        }

        User mentee = portfolio.getUser();
        ChatRoom chatRoom = ChatRoom.builder()
                .portfolio(portfolio)
                .mentor(mentor)
                .mentee(mentee)
                .build();

        ChatRoom saved = chatRoomRepository.save(chatRoom);
        return convertToDTO(saved);
    }

    // 첨삭 수락 시 사용되는 로직: 상태 조건 없이 채팅방만 생성
    @Override
    @Transactional
    public ChatRoom createChatRoomIfNotExists(Long mentorId, Long menteeId, Long portfolioId) {
        Optional<ChatRoom> existingRoom = chatRoomRepository
                .findByMentor_UserIdAndMentee_UserIdAndPortfolio_PortfolioId(mentorId, menteeId, portfolioId);

        if (existingRoom.isPresent()) {
            return existingRoom.get();
        }

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
                .build();

        return chatRoomRepository.save(newRoom);
    }

    // 채팅방 목록 조회
    @Override
    public List<ChatRoomResponseDTO> findAllChatRoomsByUserId(Long userId) {
        List<ChatRoom> rooms = chatRoomRepository.findByMentor_UserIdOrMentee_UserId(userId, userId);

        return rooms.stream()
                .filter(room -> room.getPortfolio() != null)
                .map(this::convertToDTO)
                .toList();
    }

    // 공통 DTO 변환 로직
    private ChatRoomResponseDTO convertToDTO(ChatRoom room) {
        return ChatRoomResponseDTO.builder()
                .chatRoomId(room.getChatRoomId())
                .portfolioId(room.getPortfolio().getPortfolioId())
                .mentorId(room.getMentor().getUserId())
                .menteeId(room.getMentee().getUserId())
                .createdAt(room.getCreatedAt())
                .updatedAt(room.getUpdatedAt())
                .build();
    }
}
