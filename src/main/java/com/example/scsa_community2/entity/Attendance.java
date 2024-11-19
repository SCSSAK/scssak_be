package com.example.scsa_community2.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "attendance_tbl")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {
    @Id
    private String userId;  // User의 기본 키를 FK이자 PK로 사용

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    @JsonBackReference // 자식으로 설정
    private User user;

    private LocalDateTime attendanceTime;  // 날짜와 시간 정보 모두 저장
}
