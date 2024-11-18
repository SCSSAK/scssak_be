package com.example.scsa_community2.dto.response;

import lombok.Data;

@Data
public class CommentResponse {
    private String commentUserId;
    private String commentUserName;
    private String commentContent;
    private String commentCreatedAt;
}
