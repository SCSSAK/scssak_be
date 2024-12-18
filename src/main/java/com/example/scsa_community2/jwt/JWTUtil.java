package com.example.scsa_community2.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

import static io.jsonwebtoken.Header.JWT_TYPE;
import static io.jsonwebtoken.Header.TYPE;
import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;
import static java.util.Base64.getEncoder;

// JWT 토큰 생성 및 검증 관련 유틸리티 클래스
@Slf4j
@Component
@RequiredArgsConstructor
public class JWTUtil {

    private final JWTValueConfig jwtValueConfig;

    //토큰을 생성하는 메소드
    public String generateToken(Authentication authentication, long expiration) {
        return Jwts.builder()
                .setHeaderParam(TYPE, JWT_TYPE) // 헤더 설정
                .setClaims(generateClaims(authentication)) // 인증 정보 기반 claim 생성
                .setIssuedAt(new Date(System.currentTimeMillis())) // 생성 일시
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // 만료 일시
                .signWith(getSigningKey()) // 비밀키 추가
                .compact();
    }


    // 사용자 정보를 추출한다.
    // jwt에 포함된 Claims을 생성해주는 코드
    private Claims generateClaims(Authentication authentication) {
        Claims claims = Jwts.claims(); // claim 생성
        claims.put("userId", authentication.getPrincipal()); // userId를 키로 사용자 정보를 claim에 저장한다.
        return claims;
    }

    //jwt 서명시 사용되는 secretkey를 생성하는 메소드
    private SecretKey getSigningKey() {
        String encodedKey = getEncoder().encodeToString(jwtValueConfig.getSecretKey().getBytes()); // 비밀키 인코딩
        return hmacShaKeyFor(encodedKey.getBytes()); // 인코딩한 값으로 SHA 생성
    }

    //액세스 토큰을 검증해주는 메서드
    public JWTValType validateToken(String token) {
        try {
            getBody(token);
            return JWTValType.VALID_JWT;
        } catch (MalformedJwtException exception) {
            log.error(exception.getMessage());
            return JWTValType.INVALID_JWT_TOKEN;
        } catch (ExpiredJwtException exception) {
            log.error(exception.getMessage());
            return JWTValType.EXPIRED_JWT_TOKEN;
        } catch (UnsupportedJwtException exception) {
            log.error(exception.getMessage());
            return JWTValType.UNSUPPORTED_JWT_TOKEN;
        } catch (IllegalArgumentException exception) {
            log.error(exception.getMessage());
            return JWTValType.EMPTY_JWT;
        }
    }

    //액세스 토큰에서 Claims정보를 추출하는 메서드
    private Claims getBody(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // 비밀키 설정
                .build()
                .parseClaimsJws(token) // claim 추출
                .getBody(); // claim 정보 추출
    }

    public String getUserFromJwt(String token) {
        Claims claims = getBody(token);
        return claims.get("userId").toString(); // userId 추출.
    }

}