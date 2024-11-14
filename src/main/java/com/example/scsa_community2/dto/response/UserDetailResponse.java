package com.example.scsa_community2.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailResponse {

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("user_semester")
    private int userSemester;

    @JsonProperty("user_company")
    private String userCompany;

    @JsonProperty("user_department")
    private String userDepartment;

    @JsonProperty("user_position")
    private String userPosition;

    @JsonProperty("user_email")
    private String userEmail;

    @JsonProperty("user_sns")
    private String userSns;

    @JsonProperty("user_message")
    private String userMessage;

    @JsonProperty("user_img")
    private String userImg;
}
