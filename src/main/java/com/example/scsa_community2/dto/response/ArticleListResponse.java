package com.example.scsa_community2.dto.response;

import lombok.Data;

import java.util.List;
@Data
public class ArticleListResponse {
    private int totalPage;
    private List<ArticleResponse> articleList;

    public ArticleListResponse(int totalPage, List<ArticleResponse> articleList) {
        this.totalPage = totalPage;
        this.articleList = articleList;
    }
}
//public class ArticleListResponse {
//    private int totalPage;
//    private List<ArticleDetailResponse> articleList;
//
//    public ArticleListResponse(int totalPage, List<ArticleDetailResponse> articleList) {
//        this.totalPage = totalPage;
//        this.articleList = articleList;
//    }
//
//    public int getTotalPage() {
//        return totalPage;
//    }
//
//    public void setTotalPage(int totalPage) {
//        this.totalPage = totalPage;
//    }
//
//    public List<ArticleDetailResponse> getArticleList() {
//        return articleList;
//    }
//
//    public void setArticleList(List<ArticleDetailResponse> articleList) {
//        this.articleList = articleList;
//    }
//}
