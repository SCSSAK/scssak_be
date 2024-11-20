package com.example.scsa_community2.service;

import com.example.scsa_community2.entity.Article;
import com.example.scsa_community2.entity.Like;
import com.example.scsa_community2.entity.User;
import com.example.scsa_community2.exception.error.BaseException;
import com.example.scsa_community2.exception.error.ErrorCode;
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
            decreaseLikeCount(article);
        } else {
            // 4. 좋아요 추가
            Like like = new Like();
            like.setArticle(article);
            like.setUser(user);
            likeRepository.save(like);
            increaseLikeCount(article);
        }
    }

    private void increaseLikeCount(Article article) {
        if (article.getArticleLikeCount() == null) {
            article.setArticleLikeCount(1);
        } else {
            article.setArticleLikeCount(article.getArticleLikeCount() + 1);
        }
        articleRepository.save(article); // 변경 사항 반영
    }

    private void decreaseLikeCount(Article article) {
        if (article.getArticleLikeCount() != null && article.getArticleLikeCount() > 0) {
            article.setArticleLikeCount(article.getArticleLikeCount() - 1);
        }
        articleRepository.save(article); // 변경 사항 반영
    }
}
