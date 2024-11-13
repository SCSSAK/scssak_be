package com.example.scsa_community2.repository;

import com.example.scsa_community2.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
