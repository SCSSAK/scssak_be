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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/article")
@RequiredArgsConstructor
public class ArticleControl {

    private final ArticleService articleService;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Void> createArticle(@RequestBody ArticleRequest articleRequest) throws Exception {
        // 현재 로그인된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new Exception("Unauthorized");
        }

// authentication.getPrincipal()이 PrincipalDetails 인스턴스를 반환하는 경우 처리
        Object principal = authentication.getPrincipal();
        User currentUser = null;

        if (principal instanceof PrincipalDetails) {
            // PrincipalDetails에서 User 객체 가져오기
            currentUser = ((PrincipalDetails) principal).getUser();
        } else if (principal instanceof String) {
            // principal이 String일 경우 (로그인되지 않은 상태로 "AnonymousUser"가 올 수 있음)
            String username = (String) principal;
            currentUser = userRepository.findById(username)
                    .orElseThrow(() -> new Exception("User not found"));
        } else {
            // 예상하지 못한 principal 타입에 대해 로그로 확인하거나 디버깅
            System.out.println("Unknown principal type: " + principal.getClass().getName());
            throw new Exception("Unknown authentication principal");
        }

// 이제 currentUser는 로그인된 User 객체가 됩니다.
// 그 후 작업을 계속 진행



        // 제목과 내용 유효성 검사: null 또는 빈 문자열 체크
        if (articleRequest.getArticleTitle() == null || articleRequest.getArticleTitle().trim().isEmpty() ||
                articleRequest.getArticleContent() == null || articleRequest.getArticleContent().trim().isEmpty()) {
            throw new Exception("InvalidInput");
        }

        // Article 엔티티 생성
        Article article = new Article();
        article.setArticleTitle(articleRequest.getArticleTitle());
        article.setArticleContent(articleRequest.getArticleContent());
        article.setArticleType(articleRequest.getArticleType());
        article.setArticleIsOpen(articleRequest.isArticleIsOpen());  // isArticleIsOpen() -> getArticleIsOpen()
        article.setArticleCreatedAt(Date.valueOf(LocalDate.now()));
        article.setUser(currentUser);  // user가 이미 존재하는지 확인하고 설정

        // 게시글 저장
        articleRepository.save(article);

        return ResponseEntity.status(HttpStatus.CREATED).build();  // 201 Created 응답
    }
}
