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
public class ArticleRequest {

    private String article_title; // snake_case
    private String article_content; // snake_case
    private int article_type; // snake_case
    private boolean article_is_open; // snake_case
    private List<MultipartFile> images;

    public String getArticleTitle() {
        return article_title;
    }

    public String getArticleContent() {
        return article_content;
    }

    public int getArticleType() {
        return article_type;
    }

    public boolean isArticleIsOpen() {
        return article_is_open;
    }
}
