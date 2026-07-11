package com.novalabs.digitalbanking.common.exception;

public class UnauthorizedOperationException extends BusinessException{
    public UnauthorizedOperationException(String message){
        super(ErrorCode.UNAUTHORIZED_OPERATION, message);
    }
}
