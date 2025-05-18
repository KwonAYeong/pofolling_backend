package com.kkks.pofolling.community.controller;

import com.kkks.pofolling.community.dto.PostLikeResponseDTO;
import com.kkks.pofolling.community.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community/post")
public class PostLikeController {

    private final PostLikeService postLikeService;
    @PostMapping("/{postId}/like/{userId}")
    public ResponseEntity<ComApiResponse<PostLikeResponseDTO>> togglePostLike(
            @PathVariable Long postId,
            @PathVariable Long userId
    ) {
        PostLikeResponseDTO result = postLikeService.togglePostLike(postId, userId);
        return ResponseEntity.ok(ComApiResponse.success(result));
    }
}
