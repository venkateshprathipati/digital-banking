package com.novalabs.digitalbanking.common.response;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String correlationId,
        String path,
        List<FieldErrorResponse> fieldErrors
) {
}
