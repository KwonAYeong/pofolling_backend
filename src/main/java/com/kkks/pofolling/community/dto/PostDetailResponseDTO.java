package com.kkks.pofolling.community.dto;

import com.kkks.pofolling.community.entity.Post;
import com.kkks.pofolling.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public class PostDetailResponseDTO {
    // 작성자 정보
    private Long userId;
    private String nickname;
    private String profileImage;

    // 게시글 정보
    private Long postId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private Integer viewCount;
    private List<String> fileUrls;

    // 댓글들
    private List<ReplyResponseDTO> replies;

    public static PostDetailResponseDTO from(Post post, List<ReplyResponseDTO> replies) {
        User user = post.getUser();

        // 첨부 파일 URL 리스트 생성
        List<String> fileUrls = Stream.of(post.getFileUrl1(), post.getFileUrl2(), post.getFileUrl3())
                .filter(url -> url != null && !url.isBlank())
                .collect(Collectors.toList());

        return new PostDetailResponseDTO(
                user.getUserId(),
                user.getNickname(),
                user.getProfileImage(),
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getViewCount(),
                fileUrls,
                replies
        );
    }
}
