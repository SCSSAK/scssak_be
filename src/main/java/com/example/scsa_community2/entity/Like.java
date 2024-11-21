package com.example.scsa_community2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "like_tbl")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(LikeId.class)
public class Like {
    @Id    // private Long articleId;
    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
