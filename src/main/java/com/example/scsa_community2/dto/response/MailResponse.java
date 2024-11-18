package com.example.scsa_community2.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MailResponse {
    private Integer semester;
    private List<UserMailInfo> users;
}
