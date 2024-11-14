//package com.example.scsa_community2.repository;
//
//import com.example.scsa_community2.entity.Article;
//import com.example.scsa_community2.entity.Like;
//import com.example.scsa_community2.entity.LikeId;
//import com.example.scsa_community2.entity.User;
//import com.example.scsa_community2.repository.ArticleRepository;
//import com.example.scsa_community2.repository.LikeRepository;
//import com.example.scsa_community2.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//public class LikeRepositoryTest {
//
//    @Autowired
//    private LikeRepository likeRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private ArticleRepository articleRepository;
//
//    @BeforeEach
//    public void setUp() {
//        User user = new User("testUser", "password123", "홍길동", true, "학생", "hong@example.com", 1,
//                "자기소개", "profile.jpg", false, 10, 2, 12);
//        userRepository.save(user);
//
//        Article article = new Article(null, user, "테스트 제목", "테스트 내용", 1, true, null, 1, 5, 3);
//        articleRepository.save(article);
//    }
//
//    @Test
//    public void testCreateLike() {
//        Article article = articleRepository.findAll().get(0);
//        User user = userRepository.findById("testUser").orElseThrow();
//
//        Like like = new Like(article, user);
//        likeRepository.save(like);
//
//        LikeId likeId = new LikeId(article.getArticleId(), "testUser");
//        Like foundLike = likeRepository.findById(likeId).orElse(null);
//        assertThat(foundLike).isNotNull();
//        assertThat(foundLike.getUser().getUserId()).isEqualTo("testUser");
//    }
//}
