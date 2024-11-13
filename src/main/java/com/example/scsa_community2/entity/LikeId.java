package com.example.scsa_community2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
//@Embeddable
public class LikeId implements Serializable {
    //    @Column(name = "art")
    private Long article;
    private String user;
//    private Long articleId;
//    private String userId;


}
