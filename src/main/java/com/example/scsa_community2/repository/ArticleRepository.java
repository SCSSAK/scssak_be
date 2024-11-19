package com.example.scsa_community2.repository;

import com.example.scsa_community2.entity.Article;
import com.example.scsa_community2.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("SELECT a FROM Article a " +
            "WHERE (:articleType IS NULL OR a.articleType = :articleType) " +
            "AND (:keyword = '' OR a.articleTitle LIKE %:keyword% OR a.articleContent LIKE %:keyword%) " +
            "AND (:writerId IS NULL OR a.user.userId = :writerId) " +
            "AND (a.articleIsOpen = true OR (a.articleSemester = :semesterId AND :semesterId IS NOT NULL))")
    Page<Article> findArticles(
            @Param("articleType") Integer articleType,
            @Param("keyword") String keyword,
            @Param("writerId") String writerId,
            @Param("semesterId") Integer semesterId,
            Pageable pageable);
}