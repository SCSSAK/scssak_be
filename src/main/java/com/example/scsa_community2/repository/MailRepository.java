package com.example.scsa_community2.repository;

import com.example.scsa_community2.entity.Mail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MailRepository extends JpaRepository<Mail, Long> {
    @Query("SELECT COUNT(m) > 0 FROM Mail m WHERE m.receiver.userId = :receiverId AND m.mailCreatedAt >= :sinceTime")
    boolean hasNewMail(@Param("receiverId") String receiverId, @Param("sinceTime") LocalDateTime sinceTime);
    List<Mail> findByReceiver_UserId(String receiverId);
}

