package com.example.scsa_community2.repository;

import com.example.scsa_community2.entity.Article;
import com.example.scsa_community2.entity.User;
import com.example.scsa_community2.repository.ArticleRepository;
import com.example.scsa_community2.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        User user = new User("testUser", "password123", "홍길동", true, "학생", "hong@example.com", 1,
                "자기소개", "profile.jpg", false, 10, 2, 12);
        userRepository.save(user);
    }

    @Test
    public void testCreateArticle() {
        User user = userRepository.findById("testUser").orElseThrow();
        Article article = new Article(null, user, "테스트 제목", "테스트 내용", 1, true, Date.valueOf(LocalDate.now()), 1, 5, 3);
        articleRepository.save(article);

        Article foundArticle = articleRepository.findById(article.getArticleId()).orElse(null);
        assertThat(foundArticle).isNotNull();
        assertThat(foundArticle.getArticleTitle()).isEqualTo("테스트 제목");
    }
}
