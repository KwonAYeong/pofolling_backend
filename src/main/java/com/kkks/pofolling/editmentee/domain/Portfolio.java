package com.kkks.pofolling.editmentee.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class Portfolio {
    private Long id;
    private Long userId;
    private String title;
    private String text;
    private String fileURL;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
