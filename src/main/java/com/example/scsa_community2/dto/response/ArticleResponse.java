package com.example.scsa_community2.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ArticleResponse {
    @JsonProperty("article_id")
    private Long articleId;

    @JsonProperty("article_type")
    private Integer articleType;

    @JsonProperty("article_title")
    private String articleTitle;

    @JsonProperty("article_content")
    private String articleContent;

    @JsonProperty("article_user_name")
    private String articleUserName;

    @JsonProperty("article_created_at")
    private String articleCreatedAt;

    @JsonProperty("article_like_count")
    private int articleLikeCount;

    @JsonProperty("article_comment_count")
    private int articleCommentCount;

    @JsonProperty("article_thumbnail")
    private String articleThumbnail;
}
