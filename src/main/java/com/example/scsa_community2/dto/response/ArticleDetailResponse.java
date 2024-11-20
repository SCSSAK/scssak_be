package com.example.scsa_community2.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ArticleDetailResponse {
    @JsonProperty("article_user_id")
    private String articleUserId;

    @JsonProperty("article_user_name")
    private String articleUserName;

    @JsonProperty("article_title")
    private String articleTitle;

    @JsonProperty("article_content")
    private String articleContent;

    @JsonProperty("article_type")
    private Integer articleType;

    @JsonProperty("article_is_open")
    private boolean articleIsOpen;

    @JsonProperty("article_created_at")
    private String articleCreatedAt;

    @JsonProperty("article_like_count")
    private int articleLikeCount;

    @JsonProperty("article_is_liked")
    private boolean articleIsLiked;

    @JsonProperty("article_image_urls")
    private List<String> articleImageUrls;

    @JsonProperty("comments")
    private List<CommentResponse> comments;
}

