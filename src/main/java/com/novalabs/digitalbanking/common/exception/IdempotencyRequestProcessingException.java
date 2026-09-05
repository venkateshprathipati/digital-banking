package com.novalabs.digitalbanking.common.exception;

public class IdempotencyRequestProcessingException extends BusinessException {

    public IdempotencyRequestProcessingException(String message) {
        super(ErrorCode.IDEMPOTENCY_REQUEST_PROCESSING, message);
    }
}
