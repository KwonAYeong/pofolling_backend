package com.kkks.pofolling.mypage.dto;

import com.kkks.pofolling.mypage.entity.PortfolioStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioListResponseDTO {

    private Long portfolioId;
    private String title;
    private PortfolioStatus status;
    private LocalDateTime updatedAt;
}
