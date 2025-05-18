package com.kkks.pofolling.community.repository;

import com.kkks.pofolling.community.entity.PostLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike,Long> {

    Page<PostLike> findByUser_UserIdAndIsLikedTrue(Long userId, Pageable pageable);
    long countByUser_UserIdAndIsLikedTrue(Long userId);
}
