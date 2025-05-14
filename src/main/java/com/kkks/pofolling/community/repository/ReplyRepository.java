package com.kkks.pofolling.community.repository;

import com.kkks.pofolling.community.entity.Post;
import com.kkks.pofolling.community.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
     List<Reply> findAllByPost_PostIdOrderByCreatedAtAsc(Long postId);

     Post findByPost_PostId(Long postId);
}
