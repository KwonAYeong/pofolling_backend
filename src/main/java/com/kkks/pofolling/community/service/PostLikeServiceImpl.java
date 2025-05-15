package com.kkks.pofolling.community.service;

import com.kkks.pofolling.community.dto.PostLikeResponseDTO;
import com.kkks.pofolling.community.entity.Post;
import com.kkks.pofolling.community.entity.PostLike;
import com.kkks.pofolling.community.repository.PostLikeRepository;
import com.kkks.pofolling.community.repository.PostRepository;
import com.kkks.pofolling.exception.BusinessException;
import com.kkks.pofolling.exception.ExceptionCode;
import com.kkks.pofolling.user.entity.User;
import com.kkks.pofolling.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class PostLikeServiceImpl implements PostLikeService{
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;

    public PostLikeServiceImpl(PostRepository postRepository, UserRepository userRepository, PostLikeRepository postLikeRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postLikeRepository = postLikeRepository;
    }

    @Override
    public PostLikeResponseDTO togglePostLike(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.POST_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        // 좋아요 했는지 확인
        Optional<PostLike> postLike = postLikeRepository.findByUserAndPost(user, post);

        boolean isLiked;

        if (postLike.isPresent()) {
            // 좋아요 되어 있음 → 취소
            postLikeRepository.delete(postLike.get());
            post.updateLikeCount(-1);
            isLiked = false;
        } else {
            // 좋아요 안되어 있음 → 등록
            PostLike newLike = PostLike.builder()
                    .user(user)
                    .post(post)
                    .isLiked(true)
                    .build();
            postLikeRepository.save(newLike);
            post.updateLikeCount(+1);
            isLiked = true;
        }

        // DTO 생성 및 리턴
        return PostLikeResponseDTO.of(isLiked, post.getLikeCount());
    }











}
