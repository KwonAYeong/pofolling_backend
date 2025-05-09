package com.kkks.pofolling.mypage.dto;

import com.kkks.pofolling.user.entity.JobType;
import lombok.*;

import java.util.List;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class MypageProfileUpdateDTO {

    private String name;
    private String nickname;
    private String profileImage;
    private String phoneNumber;
    private JobType jobType;
    private String password;
    private List<CareerDTO> careers;
    private List<EducationDTO> educations;
}
