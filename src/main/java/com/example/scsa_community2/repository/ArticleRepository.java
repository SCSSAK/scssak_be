package com.example.scsa_community2.repository;

import com.example.scsa_community2.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    // 조건에 맞는 게시글을 조회하는 쿼리 작성
    @Query("SELECT a FROM Article a WHERE "
            + "(COALESCE(:articleType, -1) = -1 OR a.articleType = :articleType) "
            + "AND a.articleIsOpen IN :openTypes "
            + "AND (COALESCE(:keyword, '') = '' OR a.articleTitle LIKE %:keyword% OR a.articleContent LIKE %:keyword%) "
            + "AND (COALESCE(:writerId, '') = '' OR a.user.userId = :writerId) "
            + "AND (a.user.userSemester = :userSemester OR :openType = 1) "
            + "ORDER BY a.articleCreatedAt DESC")
    Page<Article> findArticles(Integer articleType, Integer openType, String keyword, String writerId, String userSemester, Pageable pageable);

}
