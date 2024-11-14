package com.example.scsa_community2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_tbl")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
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

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void resetRefreshToken() {
        this.refreshToken = null;
    }

}
