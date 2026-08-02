package com.novalabs.digitalbanking.common.exception;

public class InvalidDepositAmountException extends BusinessException {

    public InvalidDepositAmountException(String message) {
        super(ErrorCode.INVALID_DEPOSIT_AMOUNT, message);
    }
}
