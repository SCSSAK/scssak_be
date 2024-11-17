package com.example.scsa_community2.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MailRequest {
    @JsonProperty("receiver_id")
    private String receiverId;

    @JsonProperty("mail_content")
    private String mailContent;
}
