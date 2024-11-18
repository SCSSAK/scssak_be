package com.example.scsa_community2.control;

import com.example.scsa_community2.dto.request.ArticleRequest;
import com.example.scsa_community2.dto.response.ArticleDetailResponse;
import com.example.scsa_community2.exception.BaseException;
import com.example.scsa_community2.jwt.PrincipalDetails;
import com.example.scsa_community2.repository.ArticleRepository;
import com.example.scsa_community2.repository.UserRepository;
import com.example.scsa_community2.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/article")
@RequiredArgsConstructor
public class ArticleControl {

    Logger logger = LoggerFactory.getLogger(ArticleControl.class);

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ArticleService articleService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createArticle(
            @ModelAttribute ArticleRequest request,
            @AuthenticationPrincipal PrincipalDetails userDetails) {

        if (userDetails == null || userDetails.getUser() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
        }

        try {
            articleService.createArticle(request, userDetails.getUser());
            return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created
        } catch (BaseException e) {
            return ResponseEntity.status(HttpStatus.valueOf(e.getErrorCode().getErrorCode())).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<?> getArticleById(
            @PathVariable("articleId") Long articleId,
            @AuthenticationPrincipal PrincipalDetails userDetails) {

        if (userDetails == null || userDetails.getUser() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
        }

        try {
            ArticleDetailResponse articleDetailResponse = articleService.getArticleById(articleId, userDetails.getUser());
            return ResponseEntity.status(HttpStatus.OK).body(articleDetailResponse); // 200 OK
        } catch (BaseException e) {
            return ResponseEntity.status(HttpStatus.valueOf(e.getErrorCode().getErrorCode())).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }



    // PUT: 게시글 수정
    @PutMapping("/{articleId}")
    public ResponseEntity<Void> updateArticle(
            @PathVariable("articleId") Long articleId,
            @ModelAttribute ArticleRequest request,
            @AuthenticationPrincipal PrincipalDetails userDetails) {

        if (userDetails == null || userDetails.getUser() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
        }

        try {
            articleService.updateArticle(articleId, request, userDetails.getUser());
            return ResponseEntity.status(HttpStatus.OK).build(); // 200 OK
        } catch (BaseException e) {
            return ResponseEntity.status(HttpStatus.valueOf(e.getErrorCode().getErrorCode())).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    // DELETE: 게시글 삭제
    @DeleteMapping("/{articleId}")
    public ResponseEntity<Void> deleteArticle(
            @PathVariable("articleId") Long articleId,
            @AuthenticationPrincipal PrincipalDetails userDetails) {

        if (userDetails == null || userDetails.getUser() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
        }

        try {
            articleService.deleteArticle(articleId, userDetails.getUser());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content
        } catch (BaseException e) {
            return ResponseEntity.status(HttpStatus.valueOf(e.getErrorCode().getErrorCode())).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

//    @GetMapping
//    public ResponseEntity<?> getArticles(
//            @RequestParam(value = "article_type", required = false) Integer articleType,
//            @RequestParam(value = "open_type", required = true) Integer openType,
//            @RequestParam(value = "keyword", required = false) String keyword,
//            @RequestParam(value = "writer_id", required = false) String writerId,
//            @RequestParam(value = "current_page", defaultValue = "1") int currentPage) {
//
//        // 로그인 체크
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();  // 401 Unauthorized
//        }
//
//        // 로그인한 사용자 정보
//        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
//        User currentUser = principalDetails.getUser();
//
//        // 페이지 설정
//        Pageable pageable = PageRequest.of(currentPage - 1, 10);  // 페이지당 10개 게시글
//        Page<Article> articlePage;
//
//        try {
//            // open_type에 따른 게시글 조회
//            if (openType == 1) {
//                // 전체 공개 게시물 목록
//                articlePage = articleRepository.findArticles(articleType, 1, keyword, writerId, currentUser.getUserSemester().getSemesterId(), pageable);
//            } else if (openType == 2) {
//                // 기수 공개 게시물 목록
//                articlePage = articleRepository.findArticles(articleType, 2, keyword, writerId, currentUser.getUserSemester().getSemesterId(), pageable);
//            } else if (openType == 3) {
//                // 전체 및 기수 공개 게시물 목록
//                if (writerId != null) {
//                    // writerId가 있을 경우 학기 비교
//                    Optional<User> writer = userRepository.findById(writerId);
//                    if (writer.isPresent()) {
//                        User writerUser = writer.get();
//                        if (!writerUser.getUserSemester().equals(currentUser.getUserSemester())) {
//                            // 학기가 다르면 전체 공개 게시물만 반환
//                            articlePage = articleRepository.findArticles(articleType, 1, keyword, writerId, currentUser.getUserSemester().getSemesterId(), pageable);
//                        } else {
//                            articlePage = articleRepository.findArticles(articleType, 3, keyword, writerId, currentUser.getUserSemester().getSemesterId(), pageable);
//                        }
//                    } else {
//                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Writer not found.");
//                    }
//                } else {
//                    articlePage = articleRepository.findArticles(articleType, 3, keyword, writerId, currentUser.getUserSemester().getSemesterId(), pageable);
//                }
//            } else {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid open_type.");
//            }
//
//            // 총 페이지 수 계산
//            int totalPage = articlePage.getTotalPages() == 0 ? 1 : articlePage.getTotalPages();
//            if (currentPage > totalPage) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Page exceeds total pages.");
//            }
//
//            // 게시글 목록 가공
//            List<ArticleResponse> articleResponses = articlePage.getContent().stream()
//                    .map(article -> {
//                        ArticleResponse response = new ArticleResponse();
//                        response.setArticleType(article.getArticleType());
//                        response.setArticleTitle(article.getArticleTitle());
//                        response.setArticleContent(article.getArticleContent().length() > 30
//                                ? article.getArticleContent().substring(0, 30) : article.getArticleContent());
//                        response.setArticleUserName(article.getUser().getUserName());
//                        response.setArticleCreatedAt(article.getArticleCreatedAt().toString());
//
//                        // 좋아요 수
//                        response.setArticleLikeCount(article.getArticleLikeCount() != null ? article.getArticleLikeCount() : 0);
//
//                        // 댓글 수
//                        response.setArticleCommentCount(article.getComments() != null ? article.getComments().size() : 0);
//
//                        // 썸네일 이미지 (이미지 목록에서 첫 번째 이미지 사용)
//                        String thumbnail = article.getImageUrls() != null && !article.getImageUrls().isEmpty()
//                                ? article.getImageUrls().get(0).getImageUrl() : ""; // 썸네일 이미지 URL
//                        response.setArticleThumbnail(thumbnail);
//
//                        return response;
//                    })
//                    .collect(Collectors.toList());
//
//            // 성공적인 응답
//            return ResponseEntity.ok(new ArticleListResponse(totalPage, articleResponses));
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: " + e.getMessage());
//        }
//    }


}
