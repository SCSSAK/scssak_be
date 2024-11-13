package com.example.scsa_community2.repository;

import com.example.scsa_community2.entity.Article;
import com.example.scsa_community2.repository.ArticleRepository;
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

    @Test
    public void testCreateArticle() {
        Article article = new Article(null, "테스트 제목", "테스트 내용", 1, true, Date.valueOf(LocalDate.now()), 1, 5, 3);
        articleRepository.save(article);

        Article foundArticle = articleRepository.findById(article.getArticleId()).orElse(null);
        assertThat(foundArticle).isNotNull();
        assertThat(foundArticle.getArticleTitle()).isEqualTo("테스트 제목");
    }
}
