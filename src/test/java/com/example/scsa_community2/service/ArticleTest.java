package com.example.scsa_community2.service;

import com.example.scsa_community2.entity.Article;
import com.example.scsa_community2.entity.User;
import com.example.scsa_community2.repository.ArticleRepository;
import com.example.scsa_community2.repository.LikeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ArticleTest {

    @MockBean // LikeRepository를 Mock으로 대체
    private LikeRepository likeRepository;

    @Autowired
    private ArticleService articleService;

    @Test
    public void testCheckIfUserLikedArticle() {
        // Given
        User user = new User();
        user.setUserId("test_user");

        Article article = new Article();
        article.setArticleId(1L);

        // Mock 동작 설정
        when(likeRepository.existsByArticleAndUser(article, user)).thenReturn(true);

        // When
        boolean isLiked = articleService.checkIfUserLikedArticle(article, user);

        // Then
        assertTrue(isLiked); // 좋아요 여부 확인
    }
}

