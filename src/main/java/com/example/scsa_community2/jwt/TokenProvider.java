package com.example.scsa_community2.jwt;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private static final String BEARER_HEADER = "Bearer ";
    private static final String BLANK = "";


    // 요청으로 부터 액세스 토큰을 추출하는 메서드
    String getAccessTokenFromRequest(HttpServletRequest request) {
        return isContainsAccessToken(request) ? getAuthorizationAccessToken(request) : null;
    }

    // 액세스 토큰이 있는지 확인하는 메서드
    private boolean isContainsAccessToken(HttpServletRequest request) {
        String authorization = request.getHeader(AUTHORIZATION);
        return authorization != null && authorization.startsWith(BEARER_HEADER);
    }

    // 액세스 토큰을 헤더로 부터 가져온다.
    private String getAuthorizationAccessToken(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION).replaceFirst(BEARER_HEADER, BLANK);
    }


}
