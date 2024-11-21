package com.example.scsa_community2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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

    @Column(length = 3000)
    private String articleContent;
    private Integer articleType;
    private Boolean articleIsOpen;
    private LocalDateTime articleCreatedAt;
    private Integer articleSemester;
    private Integer articleLikeCount;
    private Integer articleCommentCount;

    // 게시글 -> 댓글 단방향 연관관계 설정
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="article_id")
    private List<Comment> comments;

    // ImageUrl과의 단방향 관계 설정
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "article_id")  // ImageUrl 테이블에 외래 키가 생성됨
    private List<ImageUrl> imageUrls;

}
