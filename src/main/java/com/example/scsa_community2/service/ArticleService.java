package com.example.scsa_community2.service;

import com.example.scsa_community2.dto.request.CreateArticleRequest;
import com.example.scsa_community2.dto.request.UpdateArticleRequest;
import com.example.scsa_community2.dto.response.ArticleDetailResponse;
import com.example.scsa_community2.dto.response.ArticleListResponse;
import com.example.scsa_community2.dto.response.ArticleResponse;
import com.example.scsa_community2.dto.response.CommentResponse;
import com.example.scsa_community2.entity.Article;
import com.example.scsa_community2.entity.Comment;
import com.example.scsa_community2.entity.ImageUrl;
import com.example.scsa_community2.entity.User;
import com.example.scsa_community2.exception.BaseException;
import com.example.scsa_community2.exception.ErrorCode;
import com.example.scsa_community2.repository.ArticleRepository;
import com.example.scsa_community2.repository.LikeRepository;
import com.example.scsa_community2.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {

    Logger logger = LoggerFactory.getLogger(ArticleService.class);

    private final ArticleRepository articleRepository;
    private final S3Service s3Service;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;


    public void createArticle(CreateArticleRequest request, User user) {
        validateCreateRequest(request);

        // Article 생성
        Article article = new Article();
        article.setArticleTitle(request.getArticleTitle());
        article.setArticleContent(request.getArticleContent());
        article.setArticleType(request.getArticleType());
        article.setArticleIsOpen(request.isArticleIsOpen());
        article.setArticleSemester(user.getUserSemester().getSemesterId());
        article.setArticleCreatedAt(LocalDateTime.now()); // 변경된 부분
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

    private void validateCommonFields(String title, String content) {
        if (title == null || title.isEmpty()) {
            throw new BaseException(ErrorCode.INVALID_INPUT);
        }
        if (content == null || content.isEmpty()) {
            throw new BaseException(ErrorCode.INVALID_INPUT);
        }
    }


    private void validateCreateRequest(CreateArticleRequest request) {
        validateCommonFields(request.getArticleTitle(), request.getArticleContent());

        if (request.getImages() == null || request.getImages().isEmpty()) {
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

        // 좋아요 여부 확인
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
        response.setCommentId(comment.getCommentId());
        response.setCommentUserId(comment.getUser().getUserId());
        response.setCommentUserName(comment.getUser().getUserName());
        response.setCommentContent(comment.getCommentContent());
        response.setCommentCreatedAt(comment.getCommentCreatedAt().toString());
        return response;
    }

    boolean checkIfUserLikedArticle(Article article, User user) {
        // 좋아요 여부를 확인하는 로직 구현
        // Like 테이블에서 특정 게시글과 사용자의 좋아요 여부 확인
        return likeRepository.existsByArticleAndUser(article, user);
    }


    // PUT: 게시글 수정
    @Transactional
    public void updateArticle(Long articleId, UpdateArticleRequest request, User user) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_DATA));


        if (!article.getUser().getUserId().equals(user.getUserId())) {
            throw new BaseException(ErrorCode.NOT_PRIVILEGED);
        }

        validateUpdateRequest(request);

//        logger.info("requestArticleTitle: {}", request.getArticleTitle());
//        logger.info("requestArticleContent: {}", request.getArticleContent());
//        logger.info("requestArticleType: {}", request.getArticleType());
//        logger.info("requestisOpen: {}", request.isArticleIsOpen());

        // 수정 가능한 필드만 업데이트
        if (request.getArticleTitle() != null) {
            article.setArticleTitle(request.getArticleTitle());
        }
        if (request.getArticleContent() != null) {
            article.setArticleContent(request.getArticleContent());
        }
        if (request.getArticleType() != null) {
            article.setArticleType(request.getArticleType());
        }
        if (request.isArticleIsOpen() != null) {
            article.setArticleIsOpen(request.isArticleIsOpen());
        }

//        logger.info("articleTitle: {}", article.getArticleTitle());
//        logger.info("articleContent: {}", article.getArticleContent());
//        logger.info("articleType: {}", article.getArticleType());
//        logger.info("isOpen: {}", article.getArticleIsOpen());
        articleRepository.save(article);
    }


    private void validateUpdateRequest(UpdateArticleRequest request) {
        if (request.getArticleTitle() != null && request.getArticleTitle().isEmpty()) {
            throw new BaseException(ErrorCode.INVALID_INPUT);
        }
        if (request.getArticleContent() != null && request.getArticleContent().isEmpty()) {
            throw new BaseException(ErrorCode.INVALID_INPUT);
        }
        // 이미지가 선택사항일 경우
//        if (request.getImages() != null && request.getImages().isEmpty()) {
//            throw new BaseException(ErrorCode.INVALID_INPUT);
//        }
    }

    // DELETE: 게시글 삭제
    @Transactional
    public void deleteArticle(Long articleId, User user) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_DATA));

        if (!article.getUser().getUserId().equals(user.getUserId())) {
            throw new BaseException(ErrorCode.NOT_PRIVILEGED);
        }

        if (article.getImageUrls() != null && !article.getImageUrls().isEmpty()) {
            try {
                article.getImageUrls().forEach(imageUrl -> s3Service.deleteFile(imageUrl.getImageUrl()));
            } catch (Exception e) {
                logger.error("Failed to delete images from S3: {}", e.getMessage());
            }
        }

        articleRepository.delete(article);
    }


    public void validateEditPermission(Long articleId, User user) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_DATA)); // 404 Not Found

        if (!article.getUser().getUserId().equals(user.getUserId())) {
            throw new BaseException(ErrorCode.NOT_PRIVILEGED); // 401 Unauthorized
        }
    }


    public ArticleListResponse getArticles(
            User currentUser,
            Integer openType,
            Integer articleType,
            String keyword,
            String writerId,
            int currentPage
    ) {
        Pageable pageable = PageRequest.of(currentPage - 1, 10); // 한 페이지당 10개

        Integer semesterId = null;

        if (openType == 1) {
            // 전체 공개 게시물만
            semesterId = null;
        } else if (openType == 2) {
            // 기수 공개 게시물
            semesterId = currentUser.getUserSemester().getSemesterId();
        } else if (openType == 3) {
            // 전체 및 기수 공개 게시물
            if (writerId == null) {
                throw new BaseException(ErrorCode.INVALID_INPUT); // writerId가 없는 경우 에러 반환
            }

            User writer = userRepository.findById(writerId)
                    .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_DATA)); // writerId가 유효하지 않음

            if (!writer.getUserSemester().equals(currentUser.getUserSemester())) {
                semesterId = null; // 전체 공개 게시물만 반환
            } else {
                semesterId = currentUser.getUserSemester().getSemesterId(); // 전체 및 기수 공개
            }
        } else {
            throw new BaseException(ErrorCode.INVALID_INPUT); // 잘못된 openType
        }

        Page<Article> articlePage = articleRepository.findArticles(articleType, keyword, writerId, semesterId, pageable);

        // 게시글 목록 변환
        List<ArticleResponse> articles = articlePage.getContent().stream()
                .map(this::mapToArticleResponse)
                .collect(Collectors.toList());

        int totalPage = Math.max(1, articlePage.getTotalPages()); // 최소 1페이지 반환
        return new ArticleListResponse(totalPage, articles);
    }


    private ArticleResponse mapToArticleResponse(Article article) {
        ArticleResponse response = new ArticleResponse();
        response.setArticleId(article.getArticleId());
        response.setArticleType(article.getArticleType());
        response.setArticleTitle(article.getArticleTitle());
        response.setArticleContent(article.getArticleContent().length() > 30
                ? article.getArticleContent().substring(0, 30) : article.getArticleContent());
        response.setArticleUserName(article.getUser().getUserName());
        response.setArticleCreatedAt(article.getArticleCreatedAt().toString());
        response.setArticleLikeCount(article.getArticleLikeCount() != null ? article.getArticleLikeCount() : 0);
        response.setArticleCommentCount(article.getComments() != null ? article.getComments().size() : 0);
        response.setArticleThumbnail(
                article.getImageUrls() != null && !article.getImageUrls().isEmpty()
                        ? article.getImageUrls().get(0).getImageUrl() : ""); // 썸네일
        return response;
    }


}
