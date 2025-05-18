package com.kkks.pofolling.community.service;

import com.kkks.pofolling.community.dto.ReplyCreateRequestDTO;
import com.kkks.pofolling.community.dto.ReplyResponseDTO;
import com.kkks.pofolling.community.entity.Post;
import com.kkks.pofolling.community.entity.Reply;
import com.kkks.pofolling.community.repository.PostRepository;
import com.kkks.pofolling.community.repository.ReplyRepository;
import com.kkks.pofolling.exception.BusinessException;
import com.kkks.pofolling.exception.ExceptionCode;
import com.kkks.pofolling.user.entity.User;
import com.kkks.pofolling.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReplyServiceImpl implements ReplyService{
    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;


    public ReplyServiceImpl(ReplyRepository replyRepository, UserRepository userRepository, PostRepository postRepository) {
        this.replyRepository = replyRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Override
    public ReplyResponseDTO createReply(Long postId, ReplyCreateRequestDTO dto, Long userId) {
        // 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        // 게시글 조회
        Post post = postRepository.findById(postId).
                orElseThrow(() -> new BusinessException(ExceptionCode.POST_NOT_FOUND));

        // 댓글 생성 로직
        Reply reply = Reply.builder()
                .user(user)
                .post(post)
                .content(dto.getContent())
                .build();

        // 댓글 저장 및 댓글 수 증가
        Reply savedReply = replyRepository.save(reply);
        post.updateReplyCount(+1);

        return ReplyResponseDTO.from(savedReply);
    }

    @Override
    public ReplyResponseDTO updateReply(String content, Long replyId, Long userId) {
        // 댓글 조회
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.REPLY_NOT_FOUND));

        // 작성자 확인
        if (!reply.getUser().getUserId().equals(userId)) {
            throw new BusinessException(ExceptionCode.UNAUTHORIZED_REPLY);
        }

        // 댓글 수정
        reply.update(content);
        return ReplyResponseDTO.from(reply);
    }

    @Override
    public void deleteReply(Long replyId, Long userId) {
        // 댓글 조회
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.REPLY_NOT_FOUND));

        // 작성자 확인
        if (!reply.getUser().getUserId().equals(userId)) {
            throw new BusinessException(ExceptionCode.UNAUTHORIZED_REPLY);
        }

        // 댓글 삭제
        replyRepository.delete(reply);

        // post
        Post post = reply.getPost();
        post.updateReplyCount(-1);
    }

}
