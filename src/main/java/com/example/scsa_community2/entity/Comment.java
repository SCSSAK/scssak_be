package com.example.scsa_community2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comments") // "comment" 대신 "comments"로 변경
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(CommentId.class)
public class Comment {
    @Id
    private Long commentId;

    @Id
    private Long articleId;

    private String commentContent;
    private java.sql.Date commentCreatedAt;
}
