package com.kkks.pofolling.chat.dto;

import lombok.*;

import java.util.List;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class ChatMessageListResponseDTO {

    private boolean isActive;
    private List<ChatMessageResponseDTO> messages;
}
