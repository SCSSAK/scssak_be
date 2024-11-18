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
    private int userTardyCount;     // 유저의 누적 지각 횟수

    @JsonProperty("tardy_penalty")
    private int tardyPenalty;      // 지각으로 인한 벌금 (1회당 10,000원)

    @JsonProperty("absent_list")
    private List<String> absentList;   // 지각자 명단 (출석하지 않은 인원)

    @JsonProperty("notice_list")
    private List<String> noticeList;  // 공지사항 리스트
}
