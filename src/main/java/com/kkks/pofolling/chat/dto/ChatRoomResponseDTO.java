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
    private Long senderId;
    private String senderNickname;
    private String senderProfileImage;
    private String lastMessage;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
