package com.example.scsa_community2.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLogInRequest {
    @NotNull(message = "User ID는 필수 입력 값입니다.")
    @JsonProperty("id")
    private String userId;

    @NotNull(message = "Password는 필수 입력 값입니다.")
    @JsonProperty("pwd")
    private String userPwd;
}

