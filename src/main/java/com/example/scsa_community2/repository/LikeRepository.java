package com.example.scsa_community2.repository;

import com.example.scsa_community2.entity.Article;
import com.example.scsa_community2.entity.Like;
import com.example.scsa_community2.entity.LikeId;
import com.example.scsa_community2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, LikeId> {
    boolean existsByArticleAndUser(Article article, User user);
}
