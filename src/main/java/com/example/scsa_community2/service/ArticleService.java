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
import com.example.scsa_community2.exception.error.BaseException;
import com.example.scsa_community2.exception.error.GlobalErrorCode;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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


    public Long createArticle(CreateArticleRequest request, User user) {
        validateCreateRequest(request);

        // Article 생성
        Article article = new Article();
        article.setArticleTitle(request.getArticleTitle());
        article.setArticleContent(request.getArticleContent());
        article.setArticleType(request.getArticleType() != null ? request.getArticleType() : 1); // 기본값 1
        article.setArticleIsOpen(request.isArticleIsOpen() != null ? request.isArticleIsOpen() : false); // 기본값 false
        article.setArticleSemester(user.getUserSemester().getSemesterId());
        article.setArticleCreatedAt(LocalDateTime.now());
        article.setUser(user);

        // 이미지 처리
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            List<ImageUrl> imageUrls = uploadImagesToS3(request.getImages());
            article.setImageUrls(imageUrls);
        }

        // 게시글 저장
//        try {
//            Article savedArticle = articleRepository.save(article);
//            return savedArticle.getArticleId(); // 저장된 게시글 ID 반환
//        } catch (Exception e) {
//            throw new BaseException(GlobalErrorCode.INTERNAL_SERVER_ERROR);
//        }
        // 게시글 저장 (try-catch 제거)
        Article savedArticle = articleRepository.save(article);
        return savedArticle.getArticleId(); // 저장된 게시글 ID 반환
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
            throw new BaseException(GlobalErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private void validateCommonFields(String title, String content) {
        if (title == null || title.isEmpty()) {
            throw new BaseException(GlobalErrorCode.INVALID_INPUT);
        }
        if (content == null || content.isEmpty()) {
            throw new BaseException(GlobalErrorCode.INVALID_INPUT);
        }
    }


    private void validateCreateRequest(CreateArticleRequest request) {
        validateCommonFields(request.getArticleTitle(), request.getArticleContent());

        if (request.getArticleType() == null || !isValidArticleType(request.getArticleType())) {
            throw new BaseException(GlobalErrorCode.INVALID_INPUT); // 유효하지 않은 articleType
        }

        if (request.isArticleIsOpen() == null) { // null 체크
            throw new BaseException(GlobalErrorCode.INVALID_INPUT); // 유효하지 않은 공개 여부
        }

//        if (request.getImages() == null || request.getImages().isEmpty()) {
//            throw new BaseException(ErrorCode.INVALID_INPUT); // 이미지는 필수
//        }
    }

    private boolean isValidArticleType(Integer articleType) {
        // 유효한 articleType 값의 범위를 정의 (예: 1, 2, 3, 4)
        return List.of(1, 2, 3, 4, 5).contains(articleType);
    }



    public ArticleDetailResponse getArticleById(Long articleId, User user) {
        // 게시글 조회
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BaseException(GlobalErrorCode.NOT_FOUND_DATA)); // 404 Not Found

        // 변환 메서드를 호출하여 DTO 반환
        return mapToArticleDetailResponse(article, user);
    }


    // Article -> ArticleDetailResponse 변환 메서드
    private ArticleDetailResponse mapToArticleDetailResponse(Article article, User user) {
        ArticleDetailResponse response = new ArticleDetailResponse();

        // 기본 정보 설정
        response.setArticleUserId(article.getUser().getUserId());
        response.setArticleUserName(article.getUser().getUserName());
        response.setArticleTitle(article.getArticleTitle());
        response.setArticleContent(article.getArticleContent());
        response.setArticleType(article.getArticleType());
        response.setArticleIsOpen(article.getArticleIsOpen()); // getter 사용
        response.setArticleCreatedAt(article.getArticleCreatedAt().toString());
        response.setArticleLikeCount(article.getArticleLikeCount() != null ? article.getArticleLikeCount() : 0);

        // 좋아요 여부 확인
        response.setArticleIsLiked(checkIfUserLikedArticle(article, user));

        // 이미지 URL 리스트 변환
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

    // Comment -> CommentResponse 변환 메서드
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
                .orElseThrow(() -> new BaseException(GlobalErrorCode.NOT_FOUND_DATA));


        if (!article.getUser().getUserId().equals(user.getUserId())) {
            throw new BaseException(GlobalErrorCode.NOT_PRIVILEGED);
        }

        validateUpdateRequest(request);



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

        articleRepository.save(article);
    }


    private void validateUpdateRequest(UpdateArticleRequest request) {
        // 제목 검증
        if (request.getArticleTitle() != null && request.getArticleTitle().isEmpty()) {
            throw new BaseException(GlobalErrorCode.INVALID_INPUT);
        }

        // 내용 검증
        if (request.getArticleContent() != null && request.getArticleContent().isEmpty()) {
            throw new BaseException(GlobalErrorCode.INVALID_INPUT);
        }

        // articleType 검증
        if (request.getArticleType() != null && !isValidArticleType(request.getArticleType())) {
            throw new BaseException(GlobalErrorCode.INVALID_INPUT); // 유효하지 않은 articleType
        }

        // 이미지가 선택 사항일 경우 (주석 처리 유지)
        // if (request.getImages() != null && request.getImages().isEmpty()) {
        //     throw new BaseException(GlobalErrorCode.INVALID_INPUT);
        // }
    }


    // DELETE: 게시글 삭제
    @Transactional
    public void deleteArticle(Long articleId, User user) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BaseException(GlobalErrorCode.NOT_FOUND_DATA));

        if (!article.getUser().getUserId().equals(user.getUserId())) {
            throw new BaseException(GlobalErrorCode.NOT_PRIVILEGED);
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
                .orElseThrow(() -> new BaseException(GlobalErrorCode.NOT_FOUND_DATA)); // 404 Not Found

        if (!article.getUser().getUserId().equals(user.getUserId())) {
            throw new BaseException(GlobalErrorCode.NOT_PRIVILEGED); // 401 Unauthorized
        }
    }


    public ArticleListResponse getArticles(
            User currentUser,
            Integer openType,
            Integer articleType,
            String keyword,
            String writerId,
            int currentPage,
            int orderType
    ) {
        Pageable pageable;
        if (orderType == 2) { // 좋아요 순 정렬 후 최신순 정렬
            pageable = PageRequest.of(currentPage - 1, 10, Sort.by(
                    Sort.Order.desc("articleLikeCount"), // 첫 번째 정렬 기준: 좋아요 순
                    Sort.Order.desc("articleCreatedAt")  // 두 번째 정렬 기준: 최신순
            ));
        } else { // 최신순 정렬 (default)
            pageable = PageRequest.of(currentPage - 1, 10, Sort.by(Sort.Direction.DESC, "articleCreatedAt"));
        }

        Integer semesterId = null;

        if (openType == 1) {
            // 전체 공개 게시물만
            semesterId = null;
        } else if (openType == 2) {
            // 기수 공개 게시물만
            semesterId = currentUser.getUserSemester().getSemesterId();
        } else if (openType == 3) {
            // 전체 및 기수 공개 게시물
            if (writerId == null) {
                throw new BaseException(GlobalErrorCode.INVALID_INPUT); // writerId가 없는 경우 에러 반환
            }

            User writer = userRepository.findById(writerId)
                    .orElseThrow(() -> new BaseException(GlobalErrorCode.NOT_FOUND_DATA)); // writerId가 유효하지 않음

            if (!writer.getUserSemester().equals(currentUser.getUserSemester())) {
                semesterId = null; // 전체 공개 게시물만 반환
            } else {
                semesterId = currentUser.getUserSemester().getSemesterId(); // 전체 및 기수 공개
            }
        } else {
            throw new BaseException(GlobalErrorCode.INVALID_INPUT); // 잘못된 openType
        }

        Page<Article> articlePage = articleRepository.findArticles(articleType, keyword, writerId, semesterId, openType, pageable);

        // 게시글 목록 변환
        List<ArticleResponse> articles = articlePage.getContent().stream()
                .map(this::mapToArticleResponse)
                .collect(Collectors.toList());

        int totalPage = Math.max(1, articlePage.getTotalPages()); // 최소 1페이지 반환
        return new ArticleListResponse(totalPage, articles);
    }

    public List<ArticleResponse> getPopularArticles() {
        Pageable pageable = PageRequest.of(0, 5); // 상위 5개의 인기 게시글
        List<Article> articles = articleRepository.findPopularArticles(pageable);

        return articles.stream()
                .map(this::mapToArticleResponse)
                .collect(Collectors.toList());
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
