package com.kkks.pofolling.mypage.dto;

import com.kkks.pofolling.mypage.entity.PortfolioStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter @Builder
@NoArgsConstructor @AllArgsConstructor
public class PortfolioMentorDetailResponseDTO {

    private Long portfolioId;
    private String nickname;
    private String profileImage;
    private String title;
    private String content;
    private String fileUrl;
    private LocalDateTime requestedAt;

}
