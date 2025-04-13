package com.kkks.pofolling.edit.service;

import com.kkks.pofolling.edit.dto.PortfolioResponseDTO;
import com.kkks.pofolling.edit.entity.EditRequest;
import com.kkks.pofolling.edit.repository.EditRequestRepository;
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
    public List<PortfolioResponseDTO> getRegisteredPf(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        List<Portfolio> portfolios = portfolioRepository.findByUserAndStatus(user, REGISTERED);

        return getDtoList(portfolios);
    }

    @Override
    @Transactional
    public void requestEdit(Long portfolioId, Long menteeId) {
        //포트폴리오id와 유저id로 객체 가져오기
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 포트폴리오입니다."));
        User mentee = userRepository.findById(menteeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 멘티가 없습니다."));

        portfolio.updateStatus(REQUESTED); //포트폴리오 상태값 REQUESTED로 변경

        EditRequest editRequest = EditRequest.create(portfolio, mentee); //editRequest 엔티티 생성
        editRequestRepository.save(editRequest); //editRequest 저장
    }

    private List<PortfolioResponseDTO> getDtoList(List<Portfolio> portfolios) {
        return portfolios.stream()
                .map(PortfolioResponseDTO::from)
                .toList();
    }


}
