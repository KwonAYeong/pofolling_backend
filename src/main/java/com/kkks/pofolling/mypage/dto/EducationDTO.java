package com.kkks.pofolling.mypage.dto;

import com.kkks.pofolling.mypage.entity.EducationStatus;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class EducationDTO {

    private Long educationId;
    private String schoolName;
    private String major;
    private String degree;
    private LocalDate admissionDate;
    private LocalDate graduationDate;
    private EducationStatus educationStatus;
}
