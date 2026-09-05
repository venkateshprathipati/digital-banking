package com.novalabs.digitalbanking.common.exception;

public class IdempotencyKeyReuseException extends BusinessException {

    public IdempotencyKeyReuseException(String message) {
        super(ErrorCode.IDEMPOTENCY_KEY_REUSED, message);
    }
}
