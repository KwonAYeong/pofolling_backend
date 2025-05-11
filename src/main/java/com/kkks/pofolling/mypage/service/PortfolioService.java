package com.kkks.pofolling.mypage.service;

import com.kkks.pofolling.mypage.dto.*;
import com.kkks.pofolling.mypage.entity.Portfolio;

import java.util.List;

public interface PortfolioService {

    Long createPortfolio(Long userId, PortfolioCreateDTO createDTO);
    void updatePortfolio (Long portfolioId, PortfolioUpdateDTO updateDTO);
    void deletePortfolio (Long portfolioId);
    List<PortfolioListResponseDTO> getMyPortfolios (Long userId);
    PortfolioDetailResponseDTO getPortfolioDetail (Long portfolioId);
}
