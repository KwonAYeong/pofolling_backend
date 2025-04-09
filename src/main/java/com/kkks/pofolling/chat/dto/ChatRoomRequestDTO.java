package com.kkks.pofolling.chat.dto;

import lombok.*;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomRequestDTO {

    private Long mentorId;
    private Long portfolioId;
}
