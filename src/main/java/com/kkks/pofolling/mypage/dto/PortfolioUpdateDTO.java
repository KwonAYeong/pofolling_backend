package com.kkks.pofolling.mypage.dto;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioUpdateDTO {

    private String title;
    private String content;
    private String fileUrl;
}
