package com.example.scsa_community2.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpRequest {
    private String userId;
    private String userPwd;
    private String userName;
    private Boolean userIsStudent;
    private String userCompany;
    private String userDepartment;
    private String userPosition;
    private String userEmail;
    private Integer userSemester;
    private String userMessage;
    private String userSns;
    private String userImg;
    private Boolean userIsCp;
    private Integer userTardyCount;
    private String refreshToken;
}
