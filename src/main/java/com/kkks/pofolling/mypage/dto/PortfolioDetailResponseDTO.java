package com.kkks.pofolling.mypage.dto;

import com.kkks.pofolling.mypage.entity.PortfolioStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioDetailResponseDTO {

    private Long portfolioId;
    private String title;
    private String content;
    private String fileUrl;
    private PortfolioStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
