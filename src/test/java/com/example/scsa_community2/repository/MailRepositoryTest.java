package com.example.scsa_community2.repository;

import com.example.scsa_community2.entity.Mail;
import com.example.scsa_community2.entity.User;
import com.example.scsa_community2.repository.MailRepository;
import com.example.scsa_community2.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MailRepositoryTest {

    @Autowired
    private MailRepository mailRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        User user = new User("testUser", "password123", "홍길동", true, "학생", "hong@example.com", 1,
                "자기소개", "profile.jpg", false, 10, 2, 12);
        userRepository.save(user);
    }

    @Test
    public void testCreateMail() {
        Mail mail = new Mail(null, "receiverUser", "메일 내용", Date.valueOf(LocalDate.now()), userRepository.findById("testUser").orElse(null));
        mailRepository.save(mail);

        Mail foundMail = mailRepository.findById(mail.getMailId()).orElse(null);
        assertThat(foundMail).isNotNull();
        assertThat(foundMail.getMailContent()).isEqualTo("메일 내용");
    }
}
