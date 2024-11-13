package com.example.scsa_community2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users") // "user" 대신 "users"로 변경
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String userId;

    private String userPwd;
    private String userName;
    private Boolean userIsStudent;
    private String userJob;
    private String userEmail;
    private Integer userSemester;
    private String userMessage;
    private String userImg;
    private Boolean userIsCp;
    private Integer userPresentCount;
    private Integer userTardyCount;
    private Integer userTotalTardyCount;
}
