package com.novalabs.digitalbanking.common.exception;

public class InsufficientBalanceException extends BusinessException{
    public InsufficientBalanceException(String message){
        super(ErrorCode.INSUFFICIENT_BALANCE, message);
    }
}
