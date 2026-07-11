package com.novalabs.digitalbanking.common.exception;

public class PaymentFailedException extends BusinessException{
    public PaymentFailedException(String message){
        super(ErrorCode.PAYMENT_FAILED, message);
    }
}
