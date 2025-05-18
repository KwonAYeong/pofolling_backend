package com.kkks.pofolling.community.service;

import com.kkks.pofolling.community.dto.*;
import com.kkks.pofolling.community.entity.Post;
import com.kkks.pofolling.community.repository.PostLikeRepository;
import com.kkks.pofolling.community.repository.PostRepository;
import com.kkks.pofolling.community.repository.ReplyRepository;
import com.kkks.pofolling.exception.BusinessException;
import com.kkks.pofolling.exception.ExceptionCode;
import com.kkks.pofolling.user.entity.User;
import com.kkks.pofolling.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional()
@Slf4j
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;
    private final FileService fileService;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, PostLikeRepository postLikeRepository, UserRepository userRepository, ReplyRepository repository, FileService fileService) {
        this.postRepository = postRepository;
        this.postLikeRepository = postLikeRepository;
        this.userRepository = userRepository;
        this.replyRepository = repository;
        this.fileService = fileService;
    }

    @Override
    public void createPost(PostCreateRequestDTO dto, Long userId) {
        // 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        // 게시글 엔티티 생성
        Post post = Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .user(user)
                .build();

        // 파일 처리 (S3 연동 전: URL 더미로 넣거나 생략 가능)
        List<MultipartFile> files = dto.getFiles();
        if (files != null && !files.isEmpty()) {
            // 파일이 3개 이하일 때만 URL 저장
            for (int i = 0; i < Math.min(3, files.size()); i++) {
                MultipartFile file = files.get(i);
                // 실제 업로드는 아직 X
                String fakeUrl = "https://dummy.url/file" + (i + 1);
                switch (i) {
                    case 0 -> post.setFileUrl1(fakeUrl);
                    case 1 -> post.setFileUrl2(fakeUrl);
                    case 2 -> post.setFileUrl3(fakeUrl);
                }
            }
        }

        // 게시글 저장
        postRepository.save(post);
        log.info("success for createPost");
    }

    @Override
    public void updatePost(Long postId, Long userId, PostUpdateRequestDTO dto) {
        // 게시글 조회
        Post post = postRepository.findById(postId).
                orElseThrow(() -> new BusinessException(ExceptionCode.UNKNOWN_ERROR));

        // 작성자 확인
        if (!post.getUser().getUserId().equals(userId)) {
            throw new BusinessException(ExceptionCode.UNKNOWN_ERROR);
        }

        // 게시글 업데이트
        post.update(dto.getTitle(), dto.getContent());

        // 파일 삭제 로직
        if (dto.getDeleteFilePosition() != null) {
            for (String position : dto.getDeleteFilePosition()) {
                switch (position) {
                    case "fileUrl1" -> post.setFileUrl1(null);
                    case "fileUrl2" -> post.setFileUrl2(null);
                    case "fileUrl3" -> post.setFileUrl3(null);
                }
            }

            // 추후 S3 삭제 로직 추가 필요.
        }

        if (dto.getUpdatedFiles() != null) {
            for (Map.Entry<String, MultipartFile> entry : dto.getUpdatedFiles().entrySet()) {
                String position = entry.getKey();
                MultipartFile file = entry.getValue();

                String uploadedUrl = fileService.uploadFile(file);

                switch (position) {
                    case "fileUrl1" -> post.setFileUrl1(uploadedUrl);
                    case "fileUrl2" -> post.setFileUrl2(uploadedUrl);
                    case "fileUrl3" -> post.setFileUrl3(uploadedUrl);
                }
            }
        }
        log.info("success for updatePost");
    }

    @Override
    public void deletePost(Long postId, Long userId) {
        // 게시글 조회
        Post post = postRepository.findById(postId).
                orElseThrow(() -> new BusinessException(ExceptionCode.UNKNOWN_ERROR));

        // 작성자 확인
        if (!post.getUser().getUserId().equals(userId)) {
            throw new BusinessException(ExceptionCode.UNKNOWN_ERROR);
        }

        // S3 저장된 파일 전체 삭제
        deleteS3Files(post);

        // DB 게시글 삭제
        postRepository.delete(post);
        log.info("success for deletePost");
    }

    @Override
    @Transactional(readOnly = true)
    public PostDetailResponseDTO getPostDetail(Long postId) {
        // 게시글 조회
        Post post = postRepository.findById(postId).
                orElseThrow(() -> new BusinessException(ExceptionCode.UNKNOWN_ERROR));

        // 댓글들 가져오기
        List<ReplyResponseDTO> replyResponseDTOS = replyRepository.findAllByPost_PostIdOrderByCreatedAtAsc(postId)
                .stream()
                .map(ReplyResponseDTO::from)
                .collect(Collectors.toList());

        return PostDetailResponseDTO.from(post, replyResponseDTOS);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostListPageResponseDTO> getPostList(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(PostListPageResponseDTO::from);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostListPageResponseDTO> getMyPosts(Long userId, Pageable pageable) {
        return postRepository.findByUser_UserId(userId, pageable)
                .map(PostListPageResponseDTO::from);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostListPageResponseDTO> getLikedPosts(Long userId, Pageable pageable) {
        return postLikeRepository.findByUser_UserIdAndIsLikedTrue(userId, pageable)
                .map(postLike -> PostListPageResponseDTO.from(postLike.getPostId()));
    }

    @Override
    public void increaseViewCount(Long postId, HttpSession session) {
        String key = "viewed_post_" + postId;

        // 30분 안에 같은 게시글 본 적 있으면 skip
        if (session.getAttribute(key) != null) {
            return;
        }

        Post post = postRepository.findById(postId).
                orElseThrow(() -> new BusinessException(ExceptionCode.UNKNOWN_ERROR));
        post.increaseViewCount();

        session.setAttribute(key, true);
        session.setMaxInactiveInterval(1800); // 세션 만료 시간: 30분 (선택)
    }


    private void deleteS3Files(Post post) {
        List<String> fileUrls = Stream.of(
                        post.getFileUrl1(),
                        post.getFileUrl2(),
                        post.getFileUrl3()
                ).filter(Objects::nonNull)
                .toList();
        for (String fileUrl : fileUrls) {
            if (fileUrl != null) {
                fileService.deleteFile(fileUrl);
            }
        }
    }
}
