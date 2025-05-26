package com.kkks.pofolling.mypage.service;

import com.kkks.pofolling.edit.entity.EditRequest;
import com.kkks.pofolling.edit.repository.EditRequestRepository;
import com.kkks.pofolling.exception.BusinessException;
import com.kkks.pofolling.exception.ExceptionCode;
import com.kkks.pofolling.mypage.dto.*;
import com.kkks.pofolling.mypage.entity.Portfolio;
import com.kkks.pofolling.mypage.entity.PortfolioStatus;
import com.kkks.pofolling.mypage.repository.PortfolioRepository;
import com.kkks.pofolling.s3.S3Uploader;
import com.kkks.pofolling.user.entity.User;
import com.kkks.pofolling.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PortfolioServiceImpl implements PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;
    private final EditRequestRepository editRequestRepository;
    private final S3Uploader s3Uploader;


    // 포트폴리오 등록
    @Override
    @Transactional
    public Long createPortfolio(Long userId, PortfolioCreateDTO createDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        Portfolio portfolio = Portfolio.builder()
                .user(user)
                .title(createDTO.getTitle())
                .content(createDTO.getContent())
                .fileUrl(createDTO.getFileUrl())
                .status(PortfolioStatus.REGISTERED)
                .build();

        Portfolio saved = portfolioRepository.save(portfolio);

        return saved.getPortfolioId();
    }

    // 포트폴리오 목록 조회
    @Override
    @Transactional(readOnly = true)
    public List<PortfolioListResponseDTO> getMyPortfolios(Long userId, PortfolioStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        List<Portfolio> portfolios;

        if (status == null) {
            portfolios = portfolioRepository.findByUser(user);
        } else  {
            portfolios = portfolioRepository.findByUserAndStatus(user, status);
        }

        return portfolios.stream()
                .map(p -> PortfolioListResponseDTO.builder()
                        .portfolioId(p.getPortfolioId())
                        .title(p.getTitle())
                        .status(p.getStatus())
                        .updatedAt(p.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    // 포트폴리오 상세 조회 (멘티용)
    @Override
    @Transactional(readOnly = true)
    public PortfolioMenteeDetailResponseDTO getMenteePortfolioDetail(Long portfolioId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.PORTFOLIO_NOT_FOUND));

        User mentee = portfolio.getUser();

        boolean canEdit = portfolio.getStatus() == PortfolioStatus.REGISTERED || portfolio.getStatus() == PortfolioStatus.COMPLETED;
        boolean canDelete = portfolio.getStatus() == PortfolioStatus.REGISTERED;

        return PortfolioMenteeDetailResponseDTO.builder()
                .portfolioId(portfolio.getPortfolioId())
                .title(portfolio.getTitle())
                .content(portfolio.getContent())
                .fileUrl(portfolio.getFileUrl())
                .profileImage(mentee.getProfileImage())
                .nickname(mentee.getNickname())
                .status(portfolio.getStatus())
                .updatedAt(portfolio.getUpdatedAt())
                .canEdit(canEdit)
                .canDelete(canDelete)
                .build();
    }

    // 포트폴리오 수정
    @Override
    @Transactional
    public void updatePortfolio(Long portfolioId, PortfolioUpdateDTO dto) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.PORTFOLIO_NOT_FOUND));

        if (portfolio.getStatus() != PortfolioStatus.REGISTERED &&
            portfolio.getStatus() != PortfolioStatus.COMPLETED) {
            throw new BusinessException(ExceptionCode.INVALID_PORTFOLIO_STATUS);
        }

        portfolio.setTitle(dto.getTitle());
        portfolio.setContent(dto.getContent());

        if (dto.getFileUrl() != null) {
            portfolio.setFileUrl(dto.getFileUrl());
        }
    }


    // 포트폴리오 삭제
    @Override
    @Transactional
    public void deletePortfolio(Long portfolioId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.PORTFOLIO_NOT_FOUND));

        s3Uploader.delete(portfolio.getFileUrl());

        portfolioRepository.delete(portfolio);
    }


    @Override
    public String getFileUrl(Long portfolioId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.PORTFOLIO_NOT_FOUND));
        return portfolio.getFileUrl();
    }

}
