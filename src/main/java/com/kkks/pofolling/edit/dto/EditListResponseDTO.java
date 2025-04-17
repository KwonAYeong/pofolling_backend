package com.kkks.pofolling.edit.dto;

import com.kkks.pofolling.edit.entity.EditRequest;
import com.kkks.pofolling.mypage.entity.Portfolio;
import com.kkks.pofolling.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class EditListResponseDTO {
    private Long editRequestId;
    private String title;
    private String profileImage;
    private String nickname;
    private LocalDateTime requestedAt;
    private String job_id;

    //==DTO 생성 메서드==//
    public static EditListResponseDTO from(EditRequest editRequest) {
        User mentee = editRequest.getMentee();
        Portfolio portfolio = editRequest.getPortfolio();
        return new EditListResponseDTO(
                editRequest.getEditRequestId(),
                portfolio.getTitle(),
                mentee.getProfileImage(),
                mentee.getNickname(),
                editRequest.getRequestedAt(),
                mentee.getJobId()
        );
    }
}


