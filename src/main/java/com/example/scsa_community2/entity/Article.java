package com.example.scsa_community2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "article_tbl")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String articleTitle;
    private String articleContent;
    private Integer articleType;
    private Boolean articleIsOpen;
    private java.sql.Date articleCreatedAt;
    private Integer articleSemester;
    private Integer articleLikeCount;
    private Integer articleCommentCount;
}
