package com.example.scsa_community2.repository;

import com.example.scsa_community2.entity.Article;
import com.example.scsa_community2.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//@Repository
//public interface ArticleRepository extends JpaRepository<Article, Long> {
//
//    @Query("SELECT a FROM Article a " +
//            "WHERE (:articleType IS NULL OR a.articleType = :articleType) " +
//            "AND (CASE WHEN :openType = 1 THEN a.articleIsOpen = TRUE " +
//            "          WHEN :openType = 2 THEN a.articleIsOpen = FALSE " +
//            "          ELSE a.articleIsOpen = TRUE END) " +
//            "AND (:keyword IS NULL OR a.articleTitle LIKE %:keyword% OR a.articleContent LIKE %:keyword%) " +
//            "AND (:writerId IS NULL OR a.user.userId = :writerId) " +
//            "AND (:writerId IS NULL OR a.articleSemester = :userSemester) " +
//            "ORDER BY a.articleCreatedAt DESC")
//    Page<Article> findArticles(
//            @Param("articleType") Integer articleType,
//            @Param("openType") Integer openType,
//            @Param("keyword") String keyword,
//            @Param("writerId") String writerId,
//            @Param("userSemester") Integer userSemester,
//            Pageable pageable);
//
//
//}
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    // 사용자 ID로 User 엔티티 조회
    @Query("SELECT u FROM User u WHERE u.userId = :writerId")
    Optional<User> findUserById(@Param("writerId") String writerId);

    // 게시글 목록 조회 (articleType, openType, keyword, writerId)
    @Query("SELECT a FROM Article a " +
            "WHERE (:articleType IS NULL OR a.articleType = :articleType) " +
            "AND (:keyword = '' OR a.articleTitle LIKE %:keyword% OR a.articleContent LIKE %:keyword%) " +
            "AND (:writerId IS NULL OR a.user.userId = :writerId) " +
            "AND (a.articleIsOpen = true OR a.articleSemester = :semesterId)")
    Page<Article> findArticles(
            @Param("articleType") Integer articleType,
            @Param("keyword") String keyword,
            @Param("writerId") String writerId,
            @Param("semesterId") Integer semesterId,
            Pageable pageable);
}