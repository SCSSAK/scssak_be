package com.example.scsa_community2.service;

import com.example.scsa_community2.dto.request.ArticleRequest;
import com.example.scsa_community2.dto.response.ArticleDetailResponse;
import com.example.scsa_community2.dto.response.CommentResponse;
import com.example.scsa_community2.entity.Article;
import com.example.scsa_community2.entity.Comment;
import com.example.scsa_community2.entity.ImageUrl;
import com.example.scsa_community2.entity.User;
import com.example.scsa_community2.exception.BaseException;
import com.example.scsa_community2.exception.ErrorCode;
import com.example.scsa_community2.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final S3Service s3Service;

    public void createArticle(ArticleRequest request, User user) {
        validateArticleRequest(request);

        // Article 생성
        Article article = new Article();
        article.setArticleTitle(request.getArticleTitle());
        article.setArticleContent(request.getArticleContent());
        article.setArticleType(request.getArticleType());
        article.setArticleIsOpen(request.isArticleIsOpen());
        article.setArticleCreatedAt(Date.valueOf(LocalDate.now()));
        article.setUser(user);

        // 이미지 처리: S3 업로드 및 URL 저장
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            List<ImageUrl> imageUrls = uploadImagesToS3(request.getImages());
            article.setImageUrls(imageUrls); // 이미지 URL 리스트 설정
        }

        // 게시글 저장
        try {
            articleRepository.save(article);
        } catch (Exception e) {
            throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private List<ImageUrl> uploadImagesToS3(List<MultipartFile> images) {
        try {
            List<ImageUrl> imageUrls = new ArrayList<>();
            for (int i = 0; i < images.size(); i++) {
                MultipartFile image = images.get(i);
                String imageUrl = s3Service.uploadFile(image); // S3 업로드 후 URL 반환
                ImageUrl imageEntity = new ImageUrl();
                imageEntity.setImageUrl(imageUrl);
                imageEntity.setImageOrder(i + 1); // 이미지 순서 설정
                imageUrls.add(imageEntity);
            }
            return imageUrls;
        } catch (Exception e) {
            throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private void validateArticleRequest(ArticleRequest request) {
        if (request.getArticleTitle() == null || request.getArticleTitle().isEmpty()) {
            throw new BaseException(ErrorCode.INVALID_INPUT);
        }
        if (request.getArticleContent() == null || request.getArticleContent().isEmpty()) {
            throw new BaseException(ErrorCode.INVALID_INPUT);
        }
    }

    public ArticleDetailResponse getArticleById(Long articleId, User user) {
        // 게시글 조회
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_DATA));

        // Article -> ArticleResponse 변환
        ArticleDetailResponse response = mapToArticleResponse(article, user);
        return response;
    }

    private ArticleDetailResponse mapToArticleResponse(Article article, User user) {
        ArticleDetailResponse response = new ArticleDetailResponse();

        // 게시글 기본 정보
        response.setArticleUserId(article.getUser().getUserId());
        response.setArticleUserName(article.getUser().getUserName());
        response.setArticleTitle(article.getArticleTitle());
        response.setArticleContent(article.getArticleContent());
        response.setArticleCreatedAt(article.getArticleCreatedAt().toString());
        response.setArticleLikeCount(article.getArticleLikeCount() != null ? article.getArticleLikeCount() : 0);

        // 좋아요 여부 (사용자가 좋아요를 눌렀는지 확인하는 로직 추가)
        response.setArticleIsLiked(checkIfUserLikedArticle(article, user));

        // 이미지 URL 리스트
        if (article.getImageUrls() != null) {
            List<String> imageUrls = article.getImageUrls().stream()
                    .map(ImageUrl::getImageUrl)
                    .collect(Collectors.toList());
            response.setArticleImageUrls(imageUrls);
        }

        // 댓글 리스트 변환
        if (article.getComments() != null) {
            List<CommentResponse> commentResponses = article.getComments().stream()
                    .map(this::mapToCommentResponse)
                    .collect(Collectors.toList());
            response.setComments(commentResponses);
        }

        return response;
    }

    private CommentResponse mapToCommentResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setCommentUserId(comment.getUser().getUserId());
        response.setCommentUserName(comment.getUser().getUserName());
        response.setCommentContent(comment.getCommentContent());
        response.setCommentCreatedAt(comment.getCommentCreatedAt().toString());
        return response;
    }

    private boolean checkIfUserLikedArticle(Article article, User user) {
        // 좋아요 여부를 확인하는 로직 구현
        // 예: article.getLikes().contains(user)
        return false; // 임시로 false 반환
    }

//    private ArticleResponse mapToResponse(Article article) {
//        // Article 엔티티를 ArticleResponse로 변환
//        ArticleResponse response = new ArticleResponse();
//        response.setArticleTitle(article.getArticleTitle());
//        response.setArticleContent(article.getArticleContent());
//        response.setArticleUserName(article.getUser().getUserName());
//        response.setArticleCreatedAt(article.getArticleCreatedAt().toString());
//        response.setArticleLikeCount(article.getArticleLikeCount() != null ? article.getArticleLikeCount() : 0);
//        response.setArticleCommentCount(article.getComments() != null ? article.getComments().size() : 0);
//
//        // 썸네일 이미지 설정 (이미지가 있을 경우 첫 번째 이미지를 사용)
//        if (article.getImageUrls() != null && !article.getImageUrls().isEmpty()) {
//            response.setArticleThumbnail(article.getImageUrls().get(0).getImageUrl());
//        }
//
//        return response;
//    }


    // PUT: 게시글 수정
    public void updateArticle(Long articleId, ArticleRequest request, User user) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_DATA));

        if (!article.getUser().getUserId().equals(user.getUserId())) {
            throw new BaseException(ErrorCode.NOT_PRIVILEGED);
        }

        validateArticleRequest(request);

        article.setArticleTitle(request.getArticleTitle());
        article.setArticleContent(request.getArticleContent());
        article.setArticleType(request.getArticleType());
        article.setArticleIsOpen(request.isArticleIsOpen());
        articleRepository.save(article);
    }

    // DELETE: 게시글 삭제
    public void deleteArticle(Long articleId, User user) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_DATA));

        if (!article.getUser().getUserId().equals(user.getUserId())) {
            throw new BaseException(ErrorCode.NOT_PRIVILEGED);
        }

        articleRepository.delete(article);
    }

}
