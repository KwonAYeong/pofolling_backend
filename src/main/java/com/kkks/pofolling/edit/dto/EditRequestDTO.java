package com.kkks.pofolling.edit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class EditRequestDTO {
    private Long portfolioId;
    private Long menteeId;
}
