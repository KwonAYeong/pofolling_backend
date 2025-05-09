package com.kkks.pofolling.chat.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomResponseDTO {

    private Long chatRoomId;
    private List<Long> portfolioIds;
    private Long mentorId;
    private Long menteeId;
    private Long senderId;
    private String senderNickname;
    private String senderProfileImage;
    private String lastMessage;
    private boolean hasNewMessage;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
