package com.kkks.pofolling.community.service;

import com.kkks.pofolling.community.dto.PostCreateRequestDTO;
import com.kkks.pofolling.community.dto.PostDetailResponseDTO;
import com.kkks.pofolling.community.dto.PostListPageResponseDTO;
import com.kkks.pofolling.community.dto.PostUpdateRequestDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    void createPost(PostCreateRequestDTO dto, Long userId);
    void updatePost(Long postId, Long userId, PostUpdateRequestDTO dto, List<MultipartFile> files);
    void deletePost(Long postId, Long userId);
    PostDetailResponseDTO getPostDetail(Long postId);
    Page<PostListPageResponseDTO> getPostList(Pageable pageable);
    void increaseViewCount(Long postId, HttpSession session);
}
