package com.example.scsa_community2.service;

import com.example.scsa_community2.entity.Article;
import com.example.scsa_community2.entity.User;
import com.example.scsa_community2.repository.ArticleRepository;
import com.example.scsa_community2.repository.LikeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
public class LikeServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private LikeRepository likeRepository;

    @InjectMocks
    private LikeService likeService;

    @Test
    public void testToggleLike() {
        // Given: 테스트 데이터 준비
        User user = new User();
        user.setUserId("test_user");

        Article article = new Article();
        article.setArticleId(1L);

        // Mock 동작 설정
        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));
        when(likeRepository.existsByArticleAndUser(article, user)).thenReturn(false);

        // When: 좋아요 토글 메서드 호출
        likeService.toggleLike(1L, user);

        // Then: 좋아요 추가 동작 검증
        verify(likeRepository, times(1)).save(any());
    }

    @Test
    public void testToggleLike_Delete() {
        // Given: 테스트 데이터 준비
        User user = new User();
        user.setUserId("test_user");

        Article article = new Article();
        article.setArticleId(1L);

        // Mock 동작 설정
        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));
        when(likeRepository.existsByArticleAndUser(article, user)).thenReturn(true);

        // When: 좋아요 토글 메서드 호출
        likeService.toggleLike(1L, user);

        // Then: 좋아요 삭제 동작 검증
        verify(likeRepository, times(1)).deleteByArticleAndUser(article, user);
    }
}
