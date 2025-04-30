package com.kkks.pofolling.edit.dto;

import com.kkks.pofolling.mypage.entity.Portfolio;
import com.kkks.pofolling.mypage.entity.PortfolioStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RegisteredPortfolioResponseDTO {
    private Long portfolioId;
    private PortfolioStatus status;
    private String title;
    private LocalDateTime updatedAt;

    public static RegisteredPortfolioResponseDTO from(Portfolio portfolio) {
        return new RegisteredPortfolioResponseDTO(
                portfolio.getPortfolioId(),
                portfolio.getStatus(),
                portfolio.getTitle(),
                portfolio.getUpdatedAt()
        );
    }
}
