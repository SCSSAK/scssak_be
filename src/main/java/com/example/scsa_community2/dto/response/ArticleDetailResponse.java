package com.example.scsa_community2.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ArticleDetailResponse {
    private String articleUserId;
    private String articleUserName;
    private String articleTitle;
    private String articleContent;
    private String articleCreatedAt;
    private int articleLikeCount;
    private boolean articleIsLiked; // 요청한 사용자의 좋아요 여부
    private List<String> articleImageUrls; // 이미지 URL 리스트
    private List<CommentResponse> comments; // 댓글 리스트
}
