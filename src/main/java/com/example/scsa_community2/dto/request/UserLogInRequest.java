package com.example.scsa_community2.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLogInRequest {
    @NonNull
    @JsonProperty("id")
    private String userId;
    @NonNull
    @JsonProperty("pwd")
    private String userPwd;
}
