package com.example.scsa_community2.repository;

import com.example.scsa_community2.entity.User;
import com.example.scsa_community2.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateUser() {
        User user = new User("testUser", "password123", "홍길동", true, "학생", "hong@example.com", 1,
                "자기소개", "profile.jpg", false, 10, 2, 12);
        userRepository.save(user);

        User foundUser = userRepository.findById("testUser").orElse(null);
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUserName()).isEqualTo("홍길동");
    }
}
