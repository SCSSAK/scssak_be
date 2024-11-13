package com.example.scsa_community2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "likes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(LikeId.class)
public class Like {
    @Id
    private Long articleId;

    @Id
    private String userId;
}
