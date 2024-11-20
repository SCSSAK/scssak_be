package com.example.scsa_community2.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    // === AUTHENTICATION & AUTHORIZATION ===
    INVALID_PASSWORD(401, "Invalid password."),
    INVALID_TOKEN(401, "토큰이 유효하지 않습니다."),
    UNAUTHORIZED(401, "잘못된 토큰입니다."),
    NOT_PRIVILEGED(403, "접근 권한이 없습니다."),

    // === VALIDATION & INPUT ===
    INVALID_INPUT(400, "유효하지 않은 입력입니다."),

    // === USER RELATED ===
    USER_NOT_FOUND(404, "존재하지 않는 유저입니다."),

    // === SEMESTER RELATED ===
    SEMESTER_NOT_FOUND(404, "존재하지 않는 학기입니다."),


    // === RESOURCE NOT FOUND ===
    NOT_FOUND(404, "페이지를 찾지 못했습니다."),
    NOT_FOUND_DATA(404, "데이터를 찾지 못했습니다."),



    // === SERVER ERRORS ===
    INTERNAL_SERVER_ERROR(500, "서버에서 오류가 발생했습니다.");

    private final int errorCode; // HTTP 상태 코드
    private final String errorMsg; // 사용자 메시지
}
