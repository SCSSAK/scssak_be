package com.example.scsa_community2.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MailListResponse {

//    @JsonProperty("receiver_id")
//    private String receiverId;
//
    @JsonProperty("receiver_name")
    private String receiverName;

    @JsonProperty("mail_list")
    private List<MailDetail> mailList;

    @Data
    @AllArgsConstructor
    public static class MailDetail {
        @JsonProperty("mail_id")
        private Long mailId;

        @JsonProperty("mail_content")
        private String mailContent;

        @JsonProperty("mail_created_at")
        private String mailCreatedAt; // 날짜 형식으로 반환

        @JsonProperty("mail_writer_id")
        private String mailWriterId;

    }
}
