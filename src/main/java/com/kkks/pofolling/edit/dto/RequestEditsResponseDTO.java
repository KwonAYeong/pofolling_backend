package com.kkks.pofolling.edit.dto;

import com.kkks.pofolling.edit.entity.EditRequest;
import com.kkks.pofolling.mypage.entity.Portfolio;
import com.kkks.pofolling.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RequestEditsResponseDTO {
    private Long editRequestId;
    private String title;
    private String profileImage;
    private String nickname;
    private String name;
    private LocalDateTime requestedAt;
    private String career;

    //==DTO 생성 메서드==//
    public static RequestEditsResponseDTO from(EditRequest editRequest) {
        User mentee = editRequest.getMentee();
        Portfolio portfolio = editRequest.getPortfolio();
        return new RequestEditsResponseDTO(
                editRequest.getEditRequestId(),
                portfolio.getTitle(),
                mentee.getProfileImage(),
                mentee.getNickname(),
                mentee.getName(),
                editRequest.getRequestedAt(),
                mentee.getCareer()
        );
    }
}


