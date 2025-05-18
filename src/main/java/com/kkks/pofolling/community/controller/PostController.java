package com.kkks.pofolling.community.controller;

import com.kkks.pofolling.community.dto.PostCreateRequestDTO;
import com.kkks.pofolling.community.dto.PostDetailResponseDTO;
import com.kkks.pofolling.community.dto.PostListPageResponseDTO;
import com.kkks.pofolling.community.dto.PostUpdateRequestDTO;
import com.kkks.pofolling.community.service.PostService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/community/post")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }


    // 게시글 리스트 가져오기
    @GetMapping
    public ResponseEntity<ComApiResponse<Page<PostListPageResponseDTO>>> getPostList(
            @PageableDefault(size = 20, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<PostListPageResponseDTO> result = postService.getPostList(pageable);
        return ResponseEntity.ok(ComApiResponse.success(result));
    }

    // 내가 쓴 글 목록 조회
    @GetMapping("/myPosts/{userId}")
    public ResponseEntity<ComApiResponse<Page<PostListPageResponseDTO>>> getMyPosts(
            @PathVariable Long userId,
            @PageableDefault(size = 10, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<PostListPageResponseDTO> result = postService.getMyPosts(userId, pageable);

        return ResponseEntity.ok(ComApiResponse.success(result));
    }

    // 내가 좋아요 누른 글 목록 조회
    @GetMapping("/likedPosts/{userId}")
    public ResponseEntity<ComApiResponse<Page<PostListPageResponseDTO>>> getLikedPosts(
            @PathVariable Long userId,
            @PageableDefault(size = 10,sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<PostListPageResponseDTO> result = postService.getLikedPosts(userId, pageable);

        return ResponseEntity.ok(ComApiResponse.success(result));
    }

    // 게시글 상세 정보 가져오기
    @GetMapping("/{postId}/{userId}")
    public ResponseEntity<ComApiResponse<PostDetailResponseDTO>> getPostDetail(
            @PathVariable Long postId,
            @PathVariable Long userId,
            HttpSession httpSession
    ) {
        // 조회수 증가 로직
        postService.increaseViewCount(postId,httpSession);

        PostDetailResponseDTO result = postService.getPostDetail(postId, userId);
        return ResponseEntity.ok(ComApiResponse.success(result));
    }

    //게시글 등록 요청
    @PostMapping("/{userId}")
    public ResponseEntity<ComApiResponse<Void>> registerPost(
            @Parameter(description = "게시글 작성 데이터", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @ModelAttribute PostCreateRequestDTO dto,
            @PathVariable Long userId
            ) {
        postService.createPost(dto, userId);
        return ResponseEntity.ok(ComApiResponse.successWithMessage(204, "게시글이 등록되었습니다."));
    }

    // 게시글 수정 요청
    @PatchMapping(value = "/{postId}/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ComApiResponse<Void>> updatePost(
            @PathVariable Long postId,
            @PathVariable Long userId,
            @RequestPart("data") PostUpdateRequestDTO dto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        postService.updatePost(postId, userId, dto, files);
        return ResponseEntity.ok(ComApiResponse.successWithMessage(204, "게시글이 수정되었습니다."));
    }

    // 게시글 삭제 요청
    @DeleteMapping("/{postId}/{userId}")
    public ResponseEntity<ComApiResponse<Void>> deletePost(
            @PathVariable Long postId,
            @PathVariable Long userId
    ) {
        postService.deletePost(postId, userId);
        return ResponseEntity.ok(ComApiResponse.successWithMessage(204, "게시글이 삭제되었습니다."));
    }



}


