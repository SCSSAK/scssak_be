package com.example.scsa_community2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "articles") // 명시적으로 "articles"로 설정
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;

    private String articleTitle;
    private String articleContent;
    private Integer articleType;
    private Boolean articleIsOpen;
    private java.sql.Date articleCreatedAt;
    private Integer articleSemester;
    private Integer articleLikeCount;
    private Integer articleCommentCount;
}
