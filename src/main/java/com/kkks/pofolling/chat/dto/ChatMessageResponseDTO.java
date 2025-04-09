package com.kkks.pofolling.chat.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResponseDTO {

    private Long messageId;
    private Long chatRoomId;
    private Long senderId;
    private String message;
    private LocalDateTime sentAt;
}
