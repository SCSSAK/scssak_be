package com.example.scsa_community2.exception.custom;

import com.example.scsa_community2.exception.error.BaseException;
import com.example.scsa_community2.exception.error.ErrorCode;

public class UnauthorizedAccessException extends BaseException {

    public UnauthorizedAccessException() {
        super(ErrorCode.UNAUTHORIZED);
    }

    public UnauthorizedAccessException(String customMessage) {
        super(ErrorCode.UNAUTHORIZED, customMessage);
    }
}
