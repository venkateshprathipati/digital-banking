package com.novalabs.digitalbanking.identity.controller;

import com.novalabs.digitalbanking.common.response.ApiResponse;
import com.novalabs.digitalbanking.common.response.ApiResponseFactory;
import com.novalabs.digitalbanking.identity.dto.LoginRequest;
import com.novalabs.digitalbanking.identity.dto.LoginResponse;
import com.novalabs.digitalbanking.identity.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.novalabs.digitalbanking.identity.utils.constants.LoginConstants.LOGIN_SUCCESS;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final ApiResponseFactory factory;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest servletRequest
    ) {
        LoginResponse response = authenticationService.authenticate(request);

        return ResponseEntity.ok(
                factory.ok(
                        response,
                        LOGIN_SUCCESS,
                        servletRequest.getRequestURI()
                ));
    }
}
