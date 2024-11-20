package com.example.scsa_community2.exception.custom;

import com.example.scsa_community2.exception.error.BaseException;
import com.example.scsa_community2.exception.error.GlobalErrorCode;

public class InvalidPasswordException extends BaseException {

    public InvalidPasswordException() {
        super(GlobalErrorCode.INVALID_PASSWORD);
    }

    public InvalidPasswordException(String customMessage) {
        super(GlobalErrorCode.INVALID_PASSWORD, customMessage);
    }
}
