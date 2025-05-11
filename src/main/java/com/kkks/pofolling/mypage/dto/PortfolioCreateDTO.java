package com.kkks.pofolling.mypage.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioCreateDTO {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotBlank(message = "파일을 업로드해주세요.")
    private String fileUrl;

}
