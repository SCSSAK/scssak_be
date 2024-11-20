package com.example.scsa_community2.exception.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {

    private final GlobalErrorCode globalErrorCode;

    // ErrorCode만 받는 생성자
    public BaseException(GlobalErrorCode globalErrorCode) {
        super(globalErrorCode.getErrorMsg());
        this.globalErrorCode = globalErrorCode;
    }

    // ErrorCode와 커스텀 메시지를 받는 생성자
    public BaseException(GlobalErrorCode globalErrorCode, String customMessage) {
        super(customMessage);
        this.globalErrorCode = globalErrorCode;
    }

    // HTTP 상태 코드를 직접 반환
    public HttpStatus getHttpStatus() {
        return HttpStatus.valueOf(globalErrorCode.getErrorCode());
    }

}
