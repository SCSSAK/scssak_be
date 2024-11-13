package com.example.scsa_community2.dto.response;

import com.example.scsa_community2.entity.User;
import jakarta.persistence.Id;
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
    private String userName;
    private Boolean userIsStudent;
    private String userJob;
    private String userEmail;
    private Integer userSemester;
    private String userMessage;
    private String userImg;
    private Boolean userIsCp;
    private Integer userTardyCount;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .userIsStudent(user.getUserIsStudent())
                .userJob(user.getUserJob())
                .userEmail(user.getUserEmail())
                .userSemester(user.getUserSemester())
                .userMessage(user.getUserMessage())
                .userImg(user.getUserImg())
                .userIsCp(user.getUserIsCp())
                .userTardyCount(user.getUserTardyCount())
                .build();
    }
}
