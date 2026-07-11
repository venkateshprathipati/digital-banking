package com.novalabs.digitalbanking.common.response;

public record FieldErrorResponse(
        String field,
        String message
) {
}
