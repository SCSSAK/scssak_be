//package com.example.scsa_community2.repository;
//
//import com.example.scsa_community2.dto.request.UserSignUpRequest;
//import com.example.scsa_community2.entity.User;
//import com.example.scsa_community2.service.UserService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
////@SpringBootTest(properties = "classpath:application-dev.properties")
//@SpringBootTest
////@DataJpaTest
////@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
////@ActiveProfiles("dev")
//public class UserRepositoryTest {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private UserService userService; // UserService 주입받기
//
//
//    @Test
//    public void testCreateUser() {
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
//                .userTardyCount(2)
//                .refreshToken("initialToken")
//                .build();
//
//        userRepository.save(user);
//
//        User foundUser = userRepository.findById("testUser").orElse(null);
//        assertThat(foundUser).isNotNull();
//        assertThat(foundUser.getUserName()).isEqualTo("홍길동");
//        assertThat(foundUser.getUserEmail()).isEqualTo("hong@example.com");
//    }
//
//    @Test
//    public void testCreateUserWithEncryptedPassword() {
//        // UserSignUpRequest 객체 생성
//        UserSignUpRequest userRequest = UserSignUpRequest.builder()
//                .userId("testUser")
//                .userPwd("password123") // 암호화될 비밀번호
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
//                .userTardyCount(2)
//                .build();
//
//        // UserService의 saveUser 메서드를 사용해 암호화된 비밀번호로 저장
//        User user = userService.saveUser(userRequest);
//
//        // 저장된 사용자 정보 확인
//        User foundUser = userRepository.findById("testUser").orElse(null);
//
//        assertThat(foundUser).isNotNull();
//        assertThat(foundUser.getUserName()).isEqualTo("홍길동");
//        assertThat(foundUser.getUserEmail()).isEqualTo("hong@example.com");
//        assertThat(foundUser.getUserPwd()).isNotEqualTo("password123"); // 원래 비밀번호와 다름을 확인
//        assertThat(foundUser.getUserPwd()).startsWith("$2a$"); // BCrypt 해시 형식 확인
//    }
//
//
//    @Test
//    public void testUserLogOut() {
//        // Given
//        UserSignUpRequest userRequest = UserSignUpRequest.builder()
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
//                .userTardyCount(2)
//                .build();
//
//        userService.saveUser(userRequest);
//        User initialUser = userRepository.findById("testUser").orElse(null);
//        assertThat(initialUser).isNotNull(); // 생성 직후 null이 아님을 확인
//
//        // When: 로그아웃 수행
//        userService.signOut("testUser");
//
//        // Then: 리프레시 토큰이 제거되었는지 확인
//        User foundUser = userRepository.findById("testUser").orElse(null);
//        assertThat(foundUser).isNotNull();
//        assertThat(foundUser.getRefreshToken()).isNull();
//    }
//
//
//}
