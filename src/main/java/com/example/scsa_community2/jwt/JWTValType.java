package com.example.scsa_community2.jwt;

// JWT 유효성 검사 결과에 대한 타입 정의 클래스
public enum JWTValType {
    VALID_JWT,
    INVALID_JWT_SIGNATURE,
    INVALID_JWT_TOKEN,
    EXPIRED_JWT_TOKEN,
    UNSUPPORTED_JWT_TOKEN,
    EMPTY_JWT
}
