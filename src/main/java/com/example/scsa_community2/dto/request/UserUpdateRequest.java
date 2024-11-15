package com.example.scsa_community2.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    private String user_pwd_current; // 현재 비밀번호
    private String user_pwd_new;     // 새로운 비밀번호
    private String user_pwd_new_check; // 새로운 비밀번호 확인
    private String user_company;    // 회사
    private String user_department; // 부서
    private String user_position;   // 직급
    private String user_email;      // 이메일
    private String user_sns;        // SNS ID
    private String user_message;    // 메시지
    private MultipartFile user_img; // 프로필 이미지
}
