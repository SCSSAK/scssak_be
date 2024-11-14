//package com.example.scsa_community2.repository;
//
//import com.example.scsa_community2.entity.Mail;
//import com.example.scsa_community2.entity.User;
//import com.example.scsa_community2.repository.MailRepository;
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
//public class MailRepositoryTest {
//
//    @Autowired
//    private MailRepository mailRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @BeforeEach
//    public void setUp() {
//        User sender = new User("senderUser", "password123", "홍길동", true, "학생", "hong@example.com", 1,
//                "자기소개", "profile.jpg", false, 10, 2, 12);
//        userRepository.save(sender);
//
//        User receiver = new User("receiverUser", "password456", "김철수", true, "직장인", "kim@example.com", 2,
//                "안녕하세요", "profile2.jpg", false, 15, 1, 16);
//        userRepository.save(receiver);
//    }
//
//    @Test
//    public void testCreateMail() {
//        User sender = userRepository.findById("senderUser").orElseThrow();
//        User receiver = userRepository.findById("receiverUser").orElseThrow();
//
//        Mail mail = new Mail(null, sender, receiver, "메일 내용", Date.valueOf(LocalDate.now()));
//        mailRepository.save(mail);
//
//        Mail foundMail = mailRepository.findById(mail.getMailId()).orElse(null);
//        assertThat(foundMail).isNotNull();
//        assertThat(foundMail.getMailContent()).isEqualTo("메일 내용");
//    }
//}
