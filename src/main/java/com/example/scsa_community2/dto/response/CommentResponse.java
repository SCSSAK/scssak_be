package com.example.scsa_community2.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CommentResponse {

    @JsonProperty("comment_id")
    private Long commentId;

    @JsonProperty("user_id")
    private String commentUserId;

    @JsonProperty("user_name")
    private String commentUserName;

    @JsonProperty("comment_content")
    private String commentContent;

    @JsonProperty("comment_created_at")
    private String commentCreatedAt;
}
