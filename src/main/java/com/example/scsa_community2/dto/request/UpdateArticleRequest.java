package com.example.scsa_community2.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateArticleRequest {

    private String article_title; // 선택적
    private String article_content; // 선택적
    private Integer article_type; // 선택적 (null 허용)
    private Boolean article_is_open; // 선택적 (null 허용)
//    private List<MultipartFile> images; // 선택적 이미지 변경 (삭제/추가)

    // Getter 메서드 오버라이드
    public String getArticleTitle() {
        return article_title;
    }

    public String getArticleContent() {
        return article_content;
    }

    public Integer getArticleType() {
        return article_type;
    }

    public Boolean isArticleIsOpen() {
        return article_is_open;
    }
}

