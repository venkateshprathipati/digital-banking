package com.novalabs.digitalbanking.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
    int status,
    boolean success,
    String message,
    T data,
    LocalDateTime timestamp,
    String correlationId,
    String path
){

}
