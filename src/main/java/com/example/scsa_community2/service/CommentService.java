package com.example.scsa_community2.service;

import com.example.scsa_community2.dto.request.CommentRequest;
import com.example.scsa_community2.entity.Article;
import com.example.scsa_community2.entity.Comment;
import com.example.scsa_community2.entity.User;
import com.example.scsa_community2.exception.BaseException;
import com.example.scsa_community2.exception.ErrorCode;
import com.example.scsa_community2.repository.ArticleRepository;
import com.example.scsa_community2.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    Logger logger = LoggerFactory.getLogger(CommentService.class);

    @Transactional
    public void createComment(Long articleId, CommentRequest request, User user) {
        // 댓글 내용 검증
        if (request.getCommentContent() == null || request.getCommentContent().trim().isEmpty()) {
            throw new BaseException(ErrorCode.INVALID_INPUT); // 400 Bad Request
        }

        // articleId로 게시글 조회
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_DATA)); // 404 Not Found

        // 댓글 생성 및 Article에 추가
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setCommentContent(request.getCommentContent());
        comment.setCommentCreatedAt(LocalDateTime.now());
        article.getComments().add(comment); // 게시글의 댓글 리스트에 추가

        // Article 저장 -> CascadeType.ALL에 의해 Comment도 저장됨
        articleRepository.save(article);
    }

    @Transactional
    public void deleteComment(Long articleId, Long commentId, User user) {
        // 게시글 조회
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_DATA)); // 404 Not Found

        // 댓글 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_DATA)); // 404 Not Found

        // 댓글이 해당 게시글의 댓글인지 확인
        if (!article.getComments().contains(comment)) {
            throw new BaseException(ErrorCode.NOT_FOUND_DATA); // 404 Not Found
        }

        // 댓글 작성자 검증
        if (!comment.getUser().getUserId().equals(user.getUserId())) {
            throw new BaseException(ErrorCode.NOT_PRIVILEGED); // 401 Unauthorized
        }

        // 게시글의 댓글 리스트에서 댓글 제거
        article.getComments().remove(comment);

        // 댓글 삭제
        commentRepository.delete(comment);
    }


}
