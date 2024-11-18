package com.example.scsa_community2.control;

import com.example.scsa_community2.dto.request.ArticleRequest;
import com.example.scsa_community2.entity.Article;
import com.example.scsa_community2.entity.User;
import com.example.scsa_community2.jwt.PrincipalDetails;
import com.example.scsa_community2.repository.ArticleRepository;
import com.example.scsa_community2.repository.UserRepository;
import com.example.scsa_community2.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/api/v1/article")
@RequiredArgsConstructor
public class ArticleControl {

    private final ArticleService articleService;
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

}
