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
            @RequestPart("articleTitle") String articleTitle,
            @RequestPart("articleContent") String articleContent,
            @RequestPart("articleType") int articleType,
            @RequestPart("articleIsOpen") boolean articleIsOpen,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) throws Exception {

        // 로그인된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new Exception("Unauthorized");
        }

        // 현재 로그인된 사용자 가져오기
        String username = (String) authentication.getPrincipal();
        User currentUser = userRepository.findById(username)
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

}
