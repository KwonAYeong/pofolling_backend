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
            return ChatRoomResponseDTO.builder()
                    .chatRoomId(room.getChatRoomId())
                    .portfolioId(room.getPortfolio().getPortfolioId())
                    .mentorId(room.getMentor().getUserId())
                    .menteeId(room.getMentee().getUserId())
                    .createdAt(room.getCreatedAt())
                    .updatedAt(room.getUpdatedAt())
                    .build();
        }

        User mentee = portfolio.getUser();
        ChatRoom chatRoom = ChatRoom.builder()
                .portfolio(portfolio)
                .mentor(mentor)
                .mentee(mentee)
                .build();

        ChatRoom saved = chatRoomRepository.save(chatRoom);

        return ChatRoomResponseDTO.builder()
                .chatRoomId(saved.getChatRoomId())
                .portfolioId(saved.getPortfolio().getPortfolioId())
                .mentorId(saved.getMentor().getUserId())
                .menteeId(saved.getMentee().getUserId())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    @Override
    public List<ChatRoomResponseDTO> findAllChatRoomsByUserId(Long userId) {
        List<ChatRoom> rooms = chatRoomRepository.findByMentor_UserIdOrMentee_UserId(userId, userId);

        return rooms.stream()
                .filter(room -> room.getPortfolio() != null)
                .map(room -> ChatRoomResponseDTO.builder()
                        .chatRoomId(room.getChatRoomId())
                        .portfolioId(room.getPortfolio().getPortfolioId())
                        .mentorId(room.getMentor().getUserId())
                        .menteeId(room.getMentee().getUserId())
                        .createdAt(room.getCreatedAt())
                        .updatedAt(room.getUpdatedAt())
                        .build()
                ).toList();
    }
}
