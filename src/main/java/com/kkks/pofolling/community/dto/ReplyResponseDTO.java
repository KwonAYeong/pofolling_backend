package com.kkks.pofolling.community.dto;

import com.kkks.pofolling.community.entity.Reply;
import com.kkks.pofolling.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReplyResponseDTO {
    private Long replyId;
    private Long userId;
    private String nickname;
    private String profileImage;
    private String content;
    private LocalDateTime updatedAt;

    public static ReplyResponseDTO from(Reply reply) {
        User user = reply.getUser();

        return new ReplyResponseDTO(
                reply.getReplyId(),
                user.getUserId(),
                user.getNickname(),
                user.getProfileImage(),
                reply.getContent(),
                reply.getUpdatedAt()
        );
    }
}
