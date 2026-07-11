package com.novalabs.digitalbanking.common.exception;

public class DuplicateResourceException extends BusinessException{

    public DuplicateResourceException(String message) {
        super(ErrorCode.DUPLICATE_ACCOUNT, message);
    }
}
