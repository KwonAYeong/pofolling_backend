package com.kkks.pofolling.community.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostLikeResponseDTO {
    private boolean isLiked;
    private int likeCount;

    //==DTO 생성 메서드==//
    public static PostLikeResponseDTO of(boolean isLiked, int likeCount) {
        return new PostLikeResponseDTO(isLiked, likeCount);
    }
}
