//package com.example.scsa_community2.repository;
//
//import com.example.scsa_community2.entity.Article;
//import com.example.scsa_community2.entity.Comment;
//import com.example.scsa_community2.entity.ImageUrl;
//import com.example.scsa_community2.entity.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.sql.Date;
//import java.time.LocalDate;
//import java.util.Arrays;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//public class ArticleRepositoryTest {
//
//    @Autowired
//    private ArticleRepository articleRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @BeforeEach
//    public void setUp() {
//        User user = User.builder()
//                .userId("testUser")
//                .userPwd("password123")
//                .userName("홍길동")
//                .userIsStudent(true)
//                .userCompany("학생")
//                .userDepartment("컴퓨터공학")
//                .userPosition("학생")
//                .userEmail("hong@example.com")
//                .userSemester(1)
//                .userMessage("자기소개")
//                .userSns("snsProfile")
//                .userImg("profile.jpg")
//                .userIsCp(false)
//                .userTardyCount(10)
//                .refreshToken("initialToken")
//                .build();
//        userRepository.save(user);
//    }
//
//    @Test
//    public void testCreateArticle() {
//        User user = userRepository.findById("testUser").orElseThrow();
//
//        // 댓글과 이미지 URL 객체 생성
//        Comment comment1 = new Comment(null, "댓글 내용 1", user, Date.valueOf(LocalDate.now()));
//        Comment comment2 = new Comment(null, "댓글 내용 2", user, Date.valueOf(LocalDate.now()));
//
//        ImageUrl imageUrl1 = new ImageUrl(null, "https://example.com/image1.jpg");
//        ImageUrl imageUrl2 = new ImageUrl(null, "https://example.com/image2.jpg");
//
//        // Article 생성자 사용
//        Article article = new Article(
//                null,
//                user,
//                "테스트 제목",
//                "테스트 내용",
//                1,
//                true,
//                Date.valueOf(LocalDate.now()),
//                1,
//                5,
//                3,
//                Arrays.asList(comment1, comment2), // 댓글 리스트 추가
//                Arrays.asList(imageUrl1, imageUrl2) // 이미지 URL 리스트 추가
//        );
//        articleRepository.save(article);
//
//        Article foundArticle = articleRepository.findById(article.getArticleId()).orElse(null);
//        assertThat(foundArticle).isNotNull();
//        assertThat(foundArticle.getArticleTitle()).isEqualTo("테스트 제목");
//        assertThat(foundArticle.getComments()).hasSize(2); // 댓글 개수 확인
//        assertThat(foundArticle.getImageUrls()).hasSize(2); // 이미지 URL 개수 확인
//    }
//}
