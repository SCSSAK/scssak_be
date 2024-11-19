package com.example.scsa_community2.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CommentRequest {

    @JsonProperty("comment_content")
    private String commentContent; // 댓글 내용
}
