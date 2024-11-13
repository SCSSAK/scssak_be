package com.example.scsa_community2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "image_urls") // 명시적으로 "image_urls"로 설정
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageUrlId;

    private Long articleId;
    private String imageUrl;
    private Integer imageOrder;
}
