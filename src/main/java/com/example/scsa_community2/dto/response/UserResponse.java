package com.example.scsa_community2.dto.response;

import com.example.scsa_community2.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private String userId;
    private String userPwd;
    private String userName;
    private Boolean userIsStudent;
    private String userCompany;
    private String userDepartment;
    private String userPosition;
    private String userEmail;
    private String userSns;
    private Integer userSemester;
    private String userMessage;
    private String userImg;
    private Boolean userIsCp;
    private Integer userTardyCount;
    private String refreshToken;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .userIsStudent(user.getUserIsStudent())
                .userCompany(user.getUserCompany())
                .userDepartment(user.getUserDepartment())
                .userPosition(user.getUserPosition())
//                .userJob(user.getUserJob())
                .userEmail(user.getUserEmail())
                .userSemester(user.getUserSemester().getSemesterId())
                .userMessage(user.getUserMessage())
                .userSns(user.getUserSns())
                .userImg(user.getUserImg())
                .userIsCp(user.getUserIsCp())
                .userTardyCount(user.getUserTardyCount())
                .build();
    }
}
