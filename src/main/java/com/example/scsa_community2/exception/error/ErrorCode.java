package com.example.scsa_community2.exception.error;

public interface ErrorCode {
    int getErrorCode(); // HTTP 상태 코드
    String getErrorMsg(); // 사용자 메시지
}