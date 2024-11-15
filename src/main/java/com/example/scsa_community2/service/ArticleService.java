package com.example.scsa_community2.service;

import com.example.scsa_community2.dto.request.ArticleRequest;
import com.example.scsa_community2.entity.Article;
import com.example.scsa_community2.entity.User;
import com.example.scsa_community2.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public void createArticle(ArticleRequest requestDto) throws Exception {
        // 현재 로그인한 사용자 정보 가져오기
        User currentUser = new User();
        currentUser.setUserId(requestDto.getUserId()); // 로그인한 사용자의 ID를 설정

        // 제목과 내용 유효성 검사
        if (requestDto.getArticleTitle().isEmpty() || requestDto.getArticleContent().isEmpty()) {
            throw new Exception("InvalidInput");
        }

        // Article 엔티티 생성
        Article article = new Article();
        article.setArticleTitle(requestDto.getArticleTitle());
        article.setArticleContent(requestDto.getArticleContent());
        article.setArticleType(requestDto.getArticleType());
        article.setArticleIsOpen(requestDto.isArticleIsOpen());
        article.setArticleCreatedAt(Date.valueOf(LocalDate.now()));
        article.setUser(currentUser);

        // 이미지 처리 로직 (선택 사항)
        List<MultipartFile> images = requestDto.getImages();
        if (images != null && !images.isEmpty()) {
            // 이미지 저장 로직
            // 파일 저장 후, 저장된 경로를 ImageUrl 엔티티와 연결하여 처리
        }

        // 게시글 저장
        articleRepository.save(article);
    }
}
