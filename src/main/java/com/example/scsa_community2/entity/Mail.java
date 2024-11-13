package com.example.scsa_community2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mails") // "mail" 대신 "mails"로 변경
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mailId;

    private String receiverId;
    private String mailContent;
    private java.sql.Date mailCreatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
