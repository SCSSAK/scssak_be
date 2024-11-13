package com.example.scsa_community2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "image_url_tbl")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageUrlId;

//    @ManyToOne
//    @JoinColumn(name = "article_id", nullable = false)
//    private Article article;

    private String imageUrl;
    private Integer imageOrder;
}
