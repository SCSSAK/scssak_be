package com.example.scsa_community2.repository;

import com.example.scsa_community2.entity.Article;
import com.example.scsa_community2.entity.Like;
import com.example.scsa_community2.entity.LikeId;
import com.example.scsa_community2.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, LikeId> {
    boolean existsByArticleAndUser(Article article, User user);

    @Transactional
    void deleteByArticleAndUser(Article article, User user);
}
