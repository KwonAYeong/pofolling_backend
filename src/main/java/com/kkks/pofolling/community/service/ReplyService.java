package com.kkks.pofolling.community.service;

import com.kkks.pofolling.community.dto.ReplyCreateRequestDTO;
import com.kkks.pofolling.community.dto.ReplyResponseDTO;
import com.kkks.pofolling.community.dto.ReplyUpdateRequestDTO;

public interface ReplyService {
    ReplyResponseDTO createReply(Long postId, ReplyCreateRequestDTO dto, Long userId);
    ReplyResponseDTO updateReply(String content, Long replyId, Long userId);
    void deleteReply(Long reply, Long userId);
}
