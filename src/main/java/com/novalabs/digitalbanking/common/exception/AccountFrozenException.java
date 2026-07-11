package com.novalabs.digitalbanking.common.exception;

public class AccountFrozenException extends BusinessException{
    public AccountFrozenException(String message){
        super(ErrorCode.ACCOUNT_FROZEN, message);
    }
}
