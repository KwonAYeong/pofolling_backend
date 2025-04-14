package com.kkks.pofolling.edit.dto;

import com.kkks.pofolling.edit.entity.EditRequest;
import com.kkks.pofolling.mypage.entity.Portfolio;
import com.kkks.pofolling.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RequestEditDetailResponseDTO {
    // 포트폴리오 정보
    private Long portfolioId;
    private String title;
    private String content;
    private String fileUrl;
    private LocalDateTime updatedAt;

    // 멘티 정보
    private Long menteeId;
    private String profileImage;
    private String nickname;
    private String name;

    //==DTO 생성 메서드==//
    public static RequestEditDetailResponseDTO from(EditRequest editRequest) {
        User mentee = editRequest.getMentee();
        Portfolio portfolio = editRequest.getPortfolio();

        return new RequestEditDetailResponseDTO(
                portfolio.getPortfolioId(),
                portfolio.getTitle(),
                portfolio.getContent(),
                portfolio.getFileUrl(),
                portfolio.getUpdatedAt(),
                mentee.getUserId(),
                mentee.getProfileImage(),
                mentee.getNickname(),
                mentee.getName()
        );
    }
}
