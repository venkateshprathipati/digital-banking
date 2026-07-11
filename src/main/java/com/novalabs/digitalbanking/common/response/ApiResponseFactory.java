package com.novalabs.digitalbanking.common.response;

import com.novalabs.digitalbanking.common.util.CorrelationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ApiResponseFactory {

    public <T> ApiResponse<T> ok(T data,String message,String path){
        return build(HttpStatus.OK,data,message,path);
    }

    public <T> ApiResponse<T> created(T data,String message,String path){
        return build(HttpStatus.CREATED,data,message,path);
    }

    private <T> ApiResponse<T> build(HttpStatus status,T data,String message,String path){
        return ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .success(status.is2xxSuccessful())
                .message(message)
                .data(data)
                .correlationId(CorrelationContext.getCorrelationId())
                .path(path)
                .build();
    }
}
