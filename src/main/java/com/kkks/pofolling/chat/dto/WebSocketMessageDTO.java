package com.kkks.pofolling.chat.dto;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class WebSocketMessageDTO {

    private Long chatRoomId;
    private Long senderId;
    private String message;
}
