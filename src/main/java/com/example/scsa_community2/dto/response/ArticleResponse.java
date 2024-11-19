package com.example.scsa_community2.dto.response;

import lombok.Data;

@Data
public class ArticleResponse {
    private Long articleId;
    private Integer articleType;
    private String articleTitle;
    private String articleContent;
    private String articleUserName;
    private String articleCreatedAt;
    private int articleLikeCount;
    private int articleCommentCount;
    private String articleThumbnail;
}
