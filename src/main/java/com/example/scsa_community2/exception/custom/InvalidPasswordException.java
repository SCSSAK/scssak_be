package com.example.scsa_community2.exception.custom;

import com.example.scsa_community2.exception.error.BaseException;
import com.example.scsa_community2.exception.error.ErrorCode;

public class InvalidPasswordException extends BaseException {

    public InvalidPasswordException() {
        super(ErrorCode.INVALID_PASSWORD);
    }

    public InvalidPasswordException(String customMessage) {
        super(ErrorCode.INVALID_PASSWORD, customMessage);
    }
}
