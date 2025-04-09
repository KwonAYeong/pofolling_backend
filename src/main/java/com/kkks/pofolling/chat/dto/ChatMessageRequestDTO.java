package com.kkks.pofolling.chat.dto;

import lombok.*;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageRequestDTO {

    private Long senderId;
    private String message;
}
