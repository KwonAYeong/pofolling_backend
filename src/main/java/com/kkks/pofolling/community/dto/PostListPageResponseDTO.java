package com.kkks.pofolling.community.dto;

import com.kkks.pofolling.community.entity.Post;
import com.kkks.pofolling.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public class PostListPageResponseDTO {
    // 작성자 정보
    private Long userId;
    private String nickname;
    private String profileImage;

    // 게시글 정보
    private Long postId;
    private String title;
    private String content;
    private Integer likeCount;
    private Integer replyCount;
    private Integer viewCount;
    private LocalDateTime createdAt;

    // 파일 존재 유무
    private boolean hasFile;

    //==DTO 생성 메서드==//
    public static PostListPageResponseDTO from(Post post) {
        User user = post.getUser();

        // 파일 존재 확인 로직
        boolean hasFile = Stream.of(post.getFileUrl1(), post.getFileUrl2(), post.getFileUrl3())
                .anyMatch(url -> url != null && !url.isBlank());

        return new PostListPageResponseDTO(
                user.getUserId(),
                user.getNickname(),
                user.getProfileImage(),
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getLikeCount(),
                post.getReplyCount(),
                post.getViewCount(),
                post.getCreatedAt(),
                hasFile
        );
    }


}
