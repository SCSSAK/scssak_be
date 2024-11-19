package com.example.scsa_community2.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ArticleListResponse {
    @JsonProperty("total_page")
    private int totalPage;

    @JsonProperty("article_list")
    private List<ArticleResponse> articleList;

    public ArticleListResponse(int totalPage, List<ArticleResponse> articleList) {
        this.totalPage = totalPage;
        this.articleList = articleList;
    }
}
