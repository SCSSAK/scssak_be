package com.example.scsa_community2.exception.custom;

import com.example.scsa_community2.exception.error.BaseException;
import com.example.scsa_community2.exception.error.GlobalErrorCode;

public class EntityNotFoundException extends BaseException {

    public EntityNotFoundException() {
        super(GlobalErrorCode.NOT_FOUND_DATA);
    }

    public EntityNotFoundException(String customMessage) {
        super(GlobalErrorCode.NOT_FOUND_DATA, customMessage);
    }
}
