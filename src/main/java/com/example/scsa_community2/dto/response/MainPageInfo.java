package com.example.scsa_community2.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class MainPageInfo {
    @JsonProperty("user_tardy_count")
    private int userTardyCount;

    @JsonProperty("tardy_penalty")
    private int tardyPenalty;

    @JsonProperty("absent_list")
    private List<String> absentList;

    @JsonProperty("notice_list")
    private List<String> noticeList;

    @JsonProperty("popular_article_list_opened_false")
    private List<ArticleResponse> popularArticleListOpenedFalse;

    @JsonProperty("popular_article_list_opened_true")
    private List<ArticleResponse> popularArticleListOpenedTrue;
}
