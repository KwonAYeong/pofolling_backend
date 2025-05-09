package com.kkks.pofolling.mypage.dto;

import com.kkks.pofolling.user.entity.JobType;
import com.kkks.pofolling.user.entity.UserRole;
import lombok.*;

import java.util.List;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class MypageProfileResponseDTO {

    private String nickName;
    private String profileImage;
    private JobType jobType;
    private UserRole role;
    private List<CareerDTO> careers;
    private List<EducationDTO> educations;
}
