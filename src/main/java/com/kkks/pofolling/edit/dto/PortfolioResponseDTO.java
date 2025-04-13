package com.kkks.pofolling.edit.dto;

import com.kkks.pofolling.mypage.entity.Portfolio;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PortfolioResponseDTO {
    private Long portfolioId;
    private String title;
    private LocalDateTime updatedAt;

    public static PortfolioResponseDTO from(Portfolio portfolio) {
        return new PortfolioResponseDTO(
                portfolio.getPortfolioId(),
                portfolio.getTitle(),
                portfolio.getUpdatedAt()
        );
    }
}
