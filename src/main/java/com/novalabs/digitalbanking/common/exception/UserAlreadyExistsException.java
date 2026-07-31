package com.novalabs.digitalbanking.common.exception;

public class UserAlreadyExistsException extends BusinessException {

    public UserAlreadyExistsException(
            ErrorCode errorCode,
            String message
    ) {
        super(errorCode, message);
    }
}
