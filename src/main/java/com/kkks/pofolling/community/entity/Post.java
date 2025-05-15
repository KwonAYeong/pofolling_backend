package com.kkks.pofolling.community.entity;

import com.kkks.pofolling.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "post")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "file_url_1")
    private String fileUrl1;

    @Column(name = "file_url_2")
    private String fileUrl2;

    @Column(name = "file_url_3")
    private String fileUrl3;

    @Column(name = "like_count")
    @Builder.Default
    private Integer likeCount = 0;

    @Column(name = "reply_count")
    @Builder.Default
    private Integer replyCount = 0;

    @Column(name = "view_count")
    @Builder.Default
    private Integer viewCount = 0;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    //==Post 내용 수정 메서드==//
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    //==Post 조회수 증가 메서드==//
    public void increaseViewCount() {
        this.viewCount += 1;
    }

    //==Post 댓글 수 변동 메서드==//
    public void updateReplyCount(int change) {
        this.replyCount = Math.max(0, this.replyCount + change);
    }

    //==Post 좋아요 수 변동 메서드==//
    public void updateLikeCount(int change) {
        this.likeCount = Math.max(0, this.likeCount + change);
    }
}


