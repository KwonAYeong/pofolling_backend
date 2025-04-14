package com.kkks.pofolling.edit.service;

import com.kkks.pofolling.chat.service.ChatRoomService;
import com.kkks.pofolling.edit.dto.RequestEditDetailResponseDTO;
import com.kkks.pofolling.edit.dto.RequestEditsResponseDTO;
import com.kkks.pofolling.edit.entity.EditRequest;
import com.kkks.pofolling.edit.repository.EditRequestRepository;
import com.kkks.pofolling.mypage.entity.Portfolio;
import com.kkks.pofolling.mypage.entity.PortfolioStatus;
import com.kkks.pofolling.user.entity.User;
import com.kkks.pofolling.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    public Page<RequestEditsResponseDTO> getRequestEditList(Pageable pageable) {
        return editRequestRepository.findByPortfolio_Status(REQUESTED, pageable)
                .map(RequestEditsResponseDTO::from);
    }

    // 요청된 첨삭 세부정보 가져오기
    @Override
    @Transactional(readOnly = true)
    public RequestEditDetailResponseDTO getRequestEditDetail(Long editRequestId) {
        EditRequest findEditRequest = editRequestRepository.findById(editRequestId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 요청첨삭입니다."));

        return RequestEditDetailResponseDTO.from(findEditRequest);
    }

    // 채팅방 생성 + 첨삭 시작
    @Override
    public void startEdit(Long mentorId, Long editRequestId) {
        User mentor = userRepository.findById(mentorId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        EditRequest er = editRequestRepository.findById(editRequestId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 요청첨삭입니다."));

        // 멘토 배정 및 상태 변경.
        er.assignMentor(mentor);
        er.getPortfolio().updateStatus(IN_PROGRESS);

        // 채팅방 자동 생성
        chatRoomService.createChatRoomIfNotExists (
                mentor.getUserId(),
                er.getMentee().getUserId(),
                er.getPortfolio().getPortfolioId()
        );
    }


}
