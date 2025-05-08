package com.kkks.pofolling.edit.service;

import com.kkks.pofolling.chat.entity.ChatRoom;
import com.kkks.pofolling.chat.service.ChatRoomService;
import com.kkks.pofolling.edit.dto.EditDetailResponseDTO;
import com.kkks.pofolling.edit.dto.EditListResponseDTO;
import com.kkks.pofolling.edit.entity.EditRequest;
import com.kkks.pofolling.edit.repository.EditRequestRepository;
import com.kkks.pofolling.exception.BusinessException;
import com.kkks.pofolling.user.entity.User;
import com.kkks.pofolling.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.kkks.pofolling.exception.ExceptionCode.*;
import static com.kkks.pofolling.mypage.entity.PortfolioStatus.*;

@Service
@Transactional()
public class EditResponseServiceImpl implements EditResponseService{

    private final EditRequestRepository editRequestRepository;
    private final UserRepository userRepository;
    private final ChatRoomService chatRoomService;

    @Autowired
    public EditResponseServiceImpl(EditRequestRepository editRequestRepository,
                                   UserRepository userRepository,
                                   ChatRoomService chatRoomService) {
        this.editRequestRepository = editRequestRepository;
        this.userRepository = userRepository;
        this.chatRoomService = chatRoomService;
    }

    // 요청된 첨삭들 가져오기
    @Override
    @Transactional(readOnly = true)
    public Page<EditListResponseDTO> getRequestEditList(Pageable pageable) {
        return editRequestRepository.findByPortfolio_Status(REQUESTED, pageable)
                .map(EditListResponseDTO::from);
    }

    // 요청된 첨삭 세부정보 가져오기
    @Override
    @Transactional(readOnly = true)
    public EditDetailResponseDTO getRequestEditDetail(Long editRequestId) {
        EditRequest findEditRequest = editRequestRepository.findById(editRequestId)
                .orElseThrow(() -> new BusinessException(EDIT_NOT_FOUND));

        return EditDetailResponseDTO.from(findEditRequest);
    }

    // 채팅방 생성 + 첨삭 시작
    @Override
    public void startEdit(Long mentorId, Long editRequestId) {
        User mentor = userRepository.findById(mentorId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
        EditRequest editRequest = editRequestRepository.findById(editRequestId)
                .orElseThrow(() -> new BusinessException(EDIT_NOT_FOUND));

        // 멘토 배정 & 상태 변경
        editRequest.assignMentor(mentor);
        editRequest.getPortfolio().updateStatus(IN_PROGRESS);

        // 채팅방 조회 또는 생성
        ChatRoom chatRoom = chatRoomService.createChatRoomIfNotExists(
                mentor.getUserId(),
                editRequest.getMentee().getUserId()
        );

        // 생성된 채팅방과 포트폴리오 연결
        editRequest.getPortfolio().setChatRoom(chatRoom);
    }


}
