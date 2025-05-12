package com.kkks.pofolling.mypage.dto;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareerDTO {

    private Long careerId;
    private String companyName;
    private String position;
    private LocalDate startedAt;
    private LocalDate endedAt;
}
