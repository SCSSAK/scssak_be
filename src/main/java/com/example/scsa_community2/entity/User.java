package com.example.scsa_community2.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @ManyToOne
    @JoinColumn(name = "user_semester")  // Semester 테이블의 semesterId를 외래 키로 참조
    private Semester userSemester;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // 부모로 설정
    private Attendance attendance;

    private String userPwd;
    private String userName;
    private Boolean userIsStudent;
    private String userCompany;
    private String userDepartment;
    private String userPosition;
    private String userEmail;
//    private Integer userSemester;
    @Column(length = 600)
    private String userMessage;
    private String userSns;
    private String userImg;
//    private Boolean userIsCp;
    private Integer userTardyCount;
    private String refreshToken;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void resetRefreshToken() {
        this.refreshToken = null;
    }

}
