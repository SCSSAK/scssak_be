package com.example.scsa_community2.exception.custom;

import com.example.scsa_community2.exception.error.BaseException;
import com.example.scsa_community2.exception.error.ErrorCode;

public class EntityNotFoundException extends BaseException {

    public EntityNotFoundException() {
        super(ErrorCode.NOT_FOUND_DATA);
    }

    public EntityNotFoundException(String customMessage) {
        super(ErrorCode.NOT_FOUND_DATA, customMessage);
    }
}
