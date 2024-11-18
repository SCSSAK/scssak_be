package com.example.scsa_community2.dto.response;

import java.util.List;

public class ArticleListResponse {
    private int totalPage;
    private List<ArticleResponse> articleList;

    public ArticleListResponse(int totalPage, List<ArticleResponse> articleList) {
        this.totalPage = totalPage;
        this.articleList = articleList;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<ArticleResponse> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<ArticleResponse> articleList) {
        this.articleList = articleList;
    }
}
