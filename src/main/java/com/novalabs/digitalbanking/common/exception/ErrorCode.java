package com.novalabs.digitalbanking.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Validation Errors
    VALIDATION_ERROR("VAL_001", "Validation failed", HttpStatus.BAD_REQUEST),
    // Business Errors
    BUSINESS_RULE_VIOLATION("BUS_001", "Business rule violation", HttpStatus.UNPROCESSABLE_ENTITY),
    // Account Errors
    ACCOUNT_NOT_FOUND("ACC_001", "Account not found", HttpStatus.NOT_FOUND),
    DUPLICATE_ACCOUNT("ACC_002", "Account already exists",HttpStatus.CONFLICT),
    ACCOUNT_FROZEN("ACC_003", "Account is frozen", HttpStatus.UNPROCESSABLE_ENTITY),
    INVALID_ACCOUNT_STATE("ACC_004", "Invalid account state", HttpStatus.UNPROCESSABLE_ENTITY),
    // Payment Errors (Future)
    INSUFFICIENT_BALANCE("PAY_001", "Insufficient balance",HttpStatus.CONFLICT),
    PAYMENT_FAILED("PAY_002", "Payment failed",HttpStatus.BAD_REQUEST),

    // Authentication/Authorization
    UNAUTHORIZED_OPERATION("AUTH_001", "Unauthorized operation",HttpStatus.FORBIDDEN),
    USERNAME_ALREADY_EXISTS("AUTH_002", "Username is already registered", HttpStatus.CONFLICT),
    EMAIL_ALREADY_EXISTS("AUTH_003", "Email is already registered", HttpStatus.CONFLICT),
    // System Errors
    INTERNAL_SERVER_ERROR("SYS_001", "Internal server error",HttpStatus.INTERNAL_SERVER_ERROR),
    DATABASE_ERROR("SYS_002", "Database error",HttpStatus.CONFLICT);

    private final String code;
    private final String defaultMessage;
    private final HttpStatus httpStatus;

}
