package com.example.scsa_community2.repository;

import com.example.scsa_community2.entity.Mail;
import com.example.scsa_community2.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MailRepository extends JpaRepository<Mail, Long> {
    @Query("SELECT COUNT(m) > 0 FROM Mail m WHERE m.receiver.userId = :receiverId AND m.mailCreatedAt >= :sinceTime")
    boolean hasNewMail(@Param("receiverId") String receiverId, @Param("sinceTime") LocalDateTime sinceTime);

    // 페이지네이션 지원
    Page<Mail> findByReceiver_UserIdOrderByMailCreatedAtDesc(String receiverId, Pageable pageable);
}


