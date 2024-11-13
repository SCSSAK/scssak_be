package com.example.scsa_community2.dto.request;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshRequest {
    @NonNull String refreshToken;
}
