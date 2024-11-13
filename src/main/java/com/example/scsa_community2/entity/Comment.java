package com.example.scsa_community2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comment_tbl")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(CommentId.class)
public class Comment {
    @Id
    private Long commentId;

    @Id
    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String commentContent;
    private java.sql.Date commentCreatedAt;
}
