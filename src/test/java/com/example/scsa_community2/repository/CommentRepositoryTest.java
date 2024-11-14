//package com.example.scsa_community2.repository;
//
//import com.example.scsa_community2.entity.Article;
//import com.example.scsa_community2.entity.Comment;
//import com.example.scsa_community2.entity.User;
//import com.example.scsa_community2.repository.ArticleRepository;
//import com.example.scsa_community2.repository.CommentRepository;
//import com.example.scsa_community2.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.sql.Date;
//import java.time.LocalDate;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//public class CommentRepositoryTest {
//
//    @Autowired
//    private CommentRepository commentRepository;
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
//        Article article = new Article(null, user, "테스트 제목", "테스트 내용", 1, true, Date.valueOf(LocalDate.now()), 1, 5, 3);
//        articleRepository.save(article);
//    }
//
//    @Test
//    public void testCreateComment() {
//        Article article = articleRepository.findAll().get(0);
//        User user = userRepository.findById("testUser").orElseThrow();
//
//        // Comment 엔티티 생성 및 저장
//        Comment comment = new Comment(null, article, user, "댓글 내용", Date.valueOf(LocalDate.now()));
//        commentRepository.save(comment);
//
//        // 저장된 Comment 엔티티 조회
//        Comment foundComment = commentRepository.findById(comment.getCommentId()).orElse(null);
//        assertThat(foundComment).isNotNull();
//        assertThat(foundComment.getCommentContent()).isEqualTo("댓글 내용");
//    }
//}
