package com.example.scsa_community2.dto.response;

import com.example.scsa_community2.jwt.Token;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLogInResponse {

    @JsonProperty("user_is_student")
    private boolean userIsStudent;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    public static UserLogInResponse of(Token token, UserResponse userData){
        return UserLogInResponse.builder()
                .userIsStudent(userData.getUserIsStudent())
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .build();
    }
}
