package com.kkks.pofolling.community.repository;

import com.kkks.pofolling.community.entity.Post;
import com.kkks.pofolling.community.entity.PostLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.kkks.pofolling.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike,Long> {

    Page<PostLike> findByUser_UserIdAndIsLikedTrue(Long userId, Pageable pageable);
    Optional<PostLike> findByUserAndPost(User user, Post post);
    Long countByPostAndIsLikedTrue(Post post); // 좋아요 true인 개수만

}
