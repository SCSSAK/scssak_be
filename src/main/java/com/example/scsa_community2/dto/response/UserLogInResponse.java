package com.example.scsa_community2.dto.response;


import com.example.scsa_community2.jwt.Token;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLogInResponse {
    @NonNull String accessToken;
    @NonNull String refreshToken;
    @NonNull UserResponse userData;

    public static UserLogInResponse of(Token token, UserResponse userData){
        return UserLogInResponse.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .userData(userData)
                .build();
    }
}
