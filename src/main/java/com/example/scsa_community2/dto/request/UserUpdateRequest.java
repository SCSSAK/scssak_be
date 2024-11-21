package com.example.scsa_community2.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Size;
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

    @Size(max = 50, message = "현재 비밀번호는 최대 50자까지 입력 가능합니다.")
    private String user_pwd_current;  // 현재 비밀번호

    @Size(max = 50, message = "새로운 비밀번호는 최대 50자까지 입력 가능합니다.")
    private String user_pwd_new;      // 새로운 비밀번호

    @Size(max = 50, message = "비밀번호 확인은 최대 50자까지 입력 가능합니다.")
    private String user_pwd_new_check; // 새로운 비밀번호 확인

    @Size(max = 50, message = "회사 이름은 최대 50자까지 입력 가능합니다.")
    private String user_company;      // 회사

    @Size(max = 50, message = "부서 이름은 최대 50자까지 입력 가능합니다.")
    private String user_department;   // 부서

    @Size(max = 50, message = "직급은 최대 50자까지 입력 가능합니다.")
    private String user_position;     // 직급

    @Size(max = 50, message = "이메일은 최대 50자까지 입력 가능합니다.")
    private String user_email;        // 이메일

    @Size(max = 50, message = "SNS 정보는 최대 50자까지 입력 가능합니다.")
    private String user_sns;          // SNS ID

    @Size(max = 200, message = "메시지는 최대 200자까지 입력 가능합니다.")
    private String user_message;      // 메시지

    private MultipartFile user_img;   // 프로필 이미지
}
