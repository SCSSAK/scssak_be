package com.example.scsa_community2.control;

import com.example.scsa_community2.dto.response.ArticleListResponse;
import com.example.scsa_community2.dto.response.ArticleResponse;
import com.example.scsa_community2.entity.Article;
import com.example.scsa_community2.entity.User;
import com.example.scsa_community2.jwt.PrincipalDetails;
import com.example.scsa_community2.repository.ArticleRepository;
import com.example.scsa_community2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/article")
@RequiredArgsConstructor
public class ArticleControl {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createArticle(
            @RequestParam("articleTitle") String articleTitle,
            @RequestParam("articleContent") String articleContent,
            @RequestParam("articleType") int articleType,
            @RequestParam("articleIsOpen") boolean articleIsOpen,
            @RequestParam(value = "images", required = false) List<MultipartFile> images) throws Exception {

        // 로그인된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new Exception("Unauthorized");
        }

        // 현재 로그인된 사용자 가져오기
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String userId = principalDetails.getUser().getUserId();
        System.out.println(userId);
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        // 제목과 내용 유효성 검사
        if (articleTitle.isEmpty() || articleContent.isEmpty()) {
            throw new Exception("InvalidInput");
        }

        // Article 엔티티 생성
        Article article = new Article();
        article.setArticleTitle(articleTitle);
        article.setArticleContent(articleContent);
        article.setArticleType(articleType);
        article.setArticleIsOpen(articleIsOpen);
        article.setArticleCreatedAt(Date.valueOf(LocalDate.now()));
        article.setUser(currentUser);

        // 게시글 저장
        articleRepository.save(article);

        // 파일 처리 (필요한 경우)
        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                // 파일 저장 로직 추가
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();  // 201 Created 응답
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<Article> getArticleById(@PathVariable("articleId") Long articleId) throws Exception {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new Exception("Article not found"));
        return ResponseEntity.ok(article);  // 200 OK 응답
    }

    @PutMapping("/{articleId}")
    public ResponseEntity<Void> updateArticle(
            @PathVariable("articleId") Long articleId,
            @RequestParam("articleTitle") String articleTitle,
            @RequestParam("articleContent") String articleContent,
            @RequestParam("articleType") int articleType,
            @RequestParam("articleIsOpen") boolean articleIsOpen,
            @RequestParam(value = "images", required = false) List<MultipartFile> images) throws Exception {

        // 로그인된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new Exception("Unauthorized");
        }

        // 현재 로그인된 사용자 가져오기
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String userId = principalDetails.getUser().getUserId();
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        // 게시글 존재 여부 확인
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new Exception("Article not found"));

        // 권한 체크 (예: 게시글 작성자만 수정 가능)
        if (!article.getUser().getUserId().equals(userId)) {
            throw new Exception("Unauthorized to update this article");
        }

        // 제목과 내용 유효성 검사
        if (articleTitle.isEmpty() || articleContent.isEmpty()) {
            throw new Exception("InvalidInput");
        }

        // 수정된 데이터로 게시글 업데이트
        article.setArticleTitle(articleTitle);
        article.setArticleContent(articleContent);
        article.setArticleType(articleType);
        article.setArticleIsOpen(articleIsOpen);
        article.setArticleCreatedAt(Date.valueOf(LocalDate.now()));  // 작성일 갱신 안 하려면 이 줄은 삭제 가능

        // 파일 처리 (필요한 경우)
        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                // 파일 저장 로직 추가
            }
        }

        // 게시글 저장
        articleRepository.save(article);

        return ResponseEntity.status(HttpStatus.OK).build();  // 200 OK 응답
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<Void> deleteArticle(@PathVariable("articleId") Long articleId) throws Exception {
        // 로그인된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new Exception("Unauthorized");
        }

        // 현재 로그인된 사용자 가져오기
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String userId = principalDetails.getUser().getUserId();
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        // 게시글 존재 여부 확인
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new Exception("Article not found"));

        // 권한 체크 (예: 게시글 작성자만 삭제 가능)
        if (!article.getUser().getUserId().equals(userId)) {
            throw new Exception("Unauthorized to delete this article");
        }

        // 게시글 삭제
        articleRepository.delete(article);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // 204 No Content 응답
    }

    @GetMapping
    public ResponseEntity<?> getArticles(
            @RequestParam(value = "article_type", required = false) Integer articleType,
            @RequestParam(value = "open_type", required = true) Integer openType,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "writer_id", required = false) String writerId,
            @RequestParam(value = "current_page", defaultValue = "1") int currentPage) {

        // 로그인 체크
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();  // 401 Unauthorized
        }

        // 로그인한 사용자 정보
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User currentUser = principalDetails.getUser();

        // 페이지 설정
        Pageable pageable = PageRequest.of(currentPage - 1, 10);  // 페이지당 10개 게시글
        Page<Article> articlePage;

        try {
            // open_type에 따른 게시글 조회
            if (openType == 1) {
                // 전체 공개 게시물 목록
                articlePage = articleRepository.findArticles(articleType, 1, keyword, writerId, currentUser.getUserSemester().getSemesterId(), pageable);
            } else if (openType == 2) {
                // 기수 공개 게시물 목록
                articlePage = articleRepository.findArticles(articleType, 2, keyword, writerId, currentUser.getUserSemester().getSemesterId(), pageable);
            } else if (openType == 3) {
                // 전체 및 기수 공개 게시물 목록
                if (writerId != null) {
                    // writerId가 있을 경우 학기 비교
                    Optional<User> writer = userRepository.findById(writerId);
                    if (writer.isPresent()) {
                        User writerUser = writer.get();
                        if (!writerUser.getUserSemester().equals(currentUser.getUserSemester())) {
                            // 학기가 다르면 전체 공개 게시물만 반환
                            articlePage = articleRepository.findArticles(articleType, 1, keyword, writerId, currentUser.getUserSemester().getSemesterId(), pageable);
                        } else {
                            articlePage = articleRepository.findArticles(articleType, 3, keyword, writerId, currentUser.getUserSemester().getSemesterId(), pageable);
                        }
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Writer not found.");
                    }
                } else {
                    articlePage = articleRepository.findArticles(articleType, 3, keyword, writerId, currentUser.getUserSemester().getSemesterId(), pageable);
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid open_type.");
            }

            // 총 페이지 수 계산
            int totalPage = articlePage.getTotalPages() == 0 ? 1 : articlePage.getTotalPages();
            if (currentPage > totalPage) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Page exceeds total pages.");
            }

            // 게시글 목록 가공
            List<ArticleResponse> articleResponses = articlePage.getContent().stream()
                    .map(article -> {
                        ArticleResponse response = new ArticleResponse();
                        response.setArticleType(article.getArticleType());
                        response.setArticleTitle(article.getArticleTitle());
                        response.setArticleContent(article.getArticleContent().length() > 30
                                ? article.getArticleContent().substring(0, 30) : article.getArticleContent());
                        response.setArticleUserName(article.getUser().getUserName());
                        response.setArticleCreatedAt(article.getArticleCreatedAt().toString());

                        // 좋아요 수
                        response.setArticleLikeCount(article.getArticleLikeCount() != null ? article.getArticleLikeCount() : 0);

                        // 댓글 수
                        response.setArticleCommentCount(article.getComments() != null ? article.getComments().size() : 0);

                        // 썸네일 이미지 (이미지 목록에서 첫 번째 이미지 사용)
                        String thumbnail = article.getImageUrls() != null && !article.getImageUrls().isEmpty()
                                ? article.getImageUrls().get(0).getImageUrl() : ""; // 썸네일 이미지 URL
                        response.setArticleThumbnail(thumbnail);

                        return response;
                    })
                    .collect(Collectors.toList());

            // 성공적인 응답
            return ResponseEntity.ok(new ArticleListResponse(totalPage, articleResponses));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: " + e.getMessage());
        }
    }


}
