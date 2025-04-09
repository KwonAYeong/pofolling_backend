package com.kkks.pofolling.chat.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomResponseDTO {
    private Long chatRoomId;
    private Long portfolioId;
    private Long mentorId;
    private Long menteeId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
