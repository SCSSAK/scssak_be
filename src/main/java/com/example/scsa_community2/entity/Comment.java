package com.example.scsa_community2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comment_tbl")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId; // 단일 기본 키 필드

//    @ManyToOne
//    @JoinColumn(name = "article_id", nullable = false)
//    private Article article;

//    @Column(value="article_id")
//    private Long article_id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String commentContent;
    private java.sql.Date commentCreatedAt;
}
