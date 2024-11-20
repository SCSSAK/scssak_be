package com.example.scsa_community2.exception.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {

    private final ErrorCode errorCode;

    // ErrorCode만 받는 생성자
    public BaseException(ErrorCode errorCode) {
        super(errorCode.getErrorMsg());
        this.errorCode = errorCode;
    }

    // ErrorCode와 커스텀 메시지를 받는 생성자
    public BaseException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }

    // HTTP 상태 코드를 직접 반환
    public HttpStatus getHttpStatus() {
        return HttpStatus.valueOf(errorCode.getErrorCode());
    }

}
