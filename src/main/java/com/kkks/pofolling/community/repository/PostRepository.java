package com.kkks.pofolling.community.repository;

import com.kkks.pofolling.community.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByUser_UserId(Long userId, Pageable pageable);
    long countByUser_UserId(Long userId);
}
