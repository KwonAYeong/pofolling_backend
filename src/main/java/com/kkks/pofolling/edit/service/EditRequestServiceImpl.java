package com.kkks.pofolling.edit.service;

import com.kkks.pofolling.edit.dto.RegisteredPortfolioResponseDTO;
import com.kkks.pofolling.edit.entity.EditRequest;
import com.kkks.pofolling.edit.repository.EditRequestRepository;
import com.kkks.pofolling.exception.BusinessException;
import com.kkks.pofolling.exception.ExceptionCode;
import com.kkks.pofolling.mypage.entity.Portfolio;
import com.kkks.pofolling.mypage.repository.PortfolioRepository;
import com.kkks.pofolling.user.entity.User;
import com.kkks.pofolling.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.kkks.pofolling.mypage.entity.PortfolioStatus.*;

@Service
@Transactional
public class EditRequestServiceImpl implements EditRequestService{
    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;
    private final EditRequestRepository editRequestRepository;
    @Autowired
    public EditRequestServiceImpl(UserRepository userRepository, PortfolioRepository portfolioRepository, EditRequestRepository editRequestRepository) {
        this.userRepository = userRepository;
        this.portfolioRepository = portfolioRepository;
        this.editRequestRepository = editRequestRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegisteredPortfolioResponseDTO> getRegisteredPf(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        List<Portfolio> portfolios = portfolioRepository.findByUserAndStatus(user, REGISTERED);

        return getDtoList(portfolios);
    }

    @Override
    @Transactional
    public void requestEdit(Long portfolioId, Long menteeId) {
        //포트폴리오id와 유저id로 객체 가져오기
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.PORTFOLIO_NOT_FOUND));
        User mentee = userRepository.findById(menteeId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        //포트폴리오 소유자 인지 검증
        if (!portfolio.getUser().getUserId().equals(menteeId)) {
            throw new BusinessException(ExceptionCode.UNAUTHORIZED_EDIT_REQUEST);
        }
        //등록된 포트폴리오가 아닐 시 예외 처리
        if (!portfolio.getStatus().equals(REGISTERED)) {
            throw new BusinessException(ExceptionCode.INVALID_EDIT_STATE);
        }

        portfolio.updateStatus(REQUESTED); //포트폴리오 상태값 REQUESTED로 변경

        EditRequest editRequest = EditRequest.create(portfolio, mentee); //editRequest 엔티티 생성
        editRequestRepository.save(editRequest); //editRequest 저장
    }

    private List<RegisteredPortfolioResponseDTO> getDtoList(List<Portfolio> portfolios) {
        return portfolios.stream()
                .map(RegisteredPortfolioResponseDTO::from)
                .toList();
    }

}
