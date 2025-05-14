package com.kkks.pofolling.mypage.dto;

import com.kkks.pofolling.mypage.entity.PortfolioStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioMenteeDetailResponseDTO {

    private Long portfolioId;
    private String nickname;
    private String profileImage;
    private String title;
    private String content;
    private String fileUrl;
    private PortfolioStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean canEdit;
    private boolean canDelete;
}
