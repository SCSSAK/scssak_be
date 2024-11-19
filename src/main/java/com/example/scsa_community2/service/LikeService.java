package com.example.scsa_community2.service;

import com.example.scsa_community2.entity.Article;
import com.example.scsa_community2.entity.Like;
import com.example.scsa_community2.entity.User;
import com.example.scsa_community2.exception.BaseException;
import com.example.scsa_community2.exception.ErrorCode;
import com.example.scsa_community2.repository.ArticleRepository;
import com.example.scsa_community2.repository.LikeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final ArticleRepository articleRepository;

    @Transactional
    public void toggleLike(Long articleId, User user) {
        // 1. 게시글 존재 여부 확인
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_DATA));

        // 2. 좋아요 여부 확인
        boolean alreadyLiked = likeRepository.existsByArticleAndUser(article, user);

        if (alreadyLiked) {
            // 3. 좋아요 삭제
            likeRepository.deleteByArticleAndUser(article, user);
        } else {
            // 4. 좋아요 추가
            Like like = new Like();
            like.setArticle(article);
            like.setUser(user);
            likeRepository.save(like);
        }
    }
}

