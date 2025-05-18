package com.kkks.pofolling.community.service;

import com.kkks.pofolling.community.dto.PostLikeResponseDTO;

public interface PostLikeService {
    PostLikeResponseDTO togglePostLike(Long postId, Long userId);
}
