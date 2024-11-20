package com.example.scsa_community2.exception.custom;

import com.example.scsa_community2.exception.error.BaseException;
import com.example.scsa_community2.exception.error.GlobalErrorCode;

public class UnauthorizedAccessException extends BaseException {

    public UnauthorizedAccessException() {
        super(GlobalErrorCode.UNAUTHORIZED);
    }

    public UnauthorizedAccessException(String customMessage) {
        super(GlobalErrorCode.UNAUTHORIZED, customMessage);
    }
}
