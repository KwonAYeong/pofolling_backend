package com.kkks.pofolling.community.controller;

import com.kkks.pofolling.community.dto.ReplyCreateRequestDTO;
import com.kkks.pofolling.community.dto.ReplyResponseDTO;
import com.kkks.pofolling.community.dto.ReplyUpdateRequestDTO;
import com.kkks.pofolling.community.repository.ReplyRepository;
import com.kkks.pofolling.community.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/community/post")
public class ReplyController {
    private final ReplyService replyService;

    @Autowired
    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @PostMapping("/{postId}/reply")
    public ResponseEntity<ComApiResponse<ReplyResponseDTO>> createReply(
            @PathVariable Long postId,
            @RequestParam Long userId,
            @RequestBody ReplyCreateRequestDTO dto
            ) {
        ReplyResponseDTO result = replyService.createReply(postId, dto, userId);
        return ResponseEntity.ok(ComApiResponse.success(result));
    }

    @PatchMapping("/reply/{replyId}")
    public ResponseEntity<ComApiResponse<ReplyResponseDTO>> updateReply(
            @PathVariable Long replyId,
            @RequestParam Long userId,
            @RequestBody ReplyUpdateRequestDTO dto
            ){
        ReplyResponseDTO result = replyService.updateReply(dto.getContent(), replyId, userId);
        return ResponseEntity.ok(ComApiResponse.success(result));
    }

    @DeleteMapping("/reply/{replyId}")
    public ResponseEntity<Void> deleteReply(
            @PathVariable Long replyId,
            @RequestParam Long userId
    ) {
        replyService.deleteReply(replyId, userId);
        return ResponseEntity.noContent().build();
    }












}
