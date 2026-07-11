package com.novalabs.digitalbanking.common.exception;

public class InvalidAccountStateException extends BusinessException{
    public InvalidAccountStateException(String message){
        super(ErrorCode.INVALID_ACCOUNT_STATE, message);
    }
}
