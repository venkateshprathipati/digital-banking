package com.novalabs.digitalbanking.identity.controller;

import com.novalabs.digitalbanking.common.response.ApiResponse;
import com.novalabs.digitalbanking.common.response.ApiResponseFactory;
import com.novalabs.digitalbanking.identity.dto.LoginRequest;
import com.novalabs.digitalbanking.identity.dto.LoginResponse;
import com.novalabs.digitalbanking.identity.dto.RegistrationRequest;
import com.novalabs.digitalbanking.identity.dto.RegistrationResponse;
import com.novalabs.digitalbanking.identity.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.novalabs.digitalbanking.identity.utils.constants.AuthenticationConstants.LOGIN_SUCCESS;
import static com.novalabs.digitalbanking.identity.utils.constants.AuthenticationConstants.REGISTER_SUCCESS;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final ApiResponseFactory factory;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegistrationResponse>> register(
            @Valid @RequestBody RegistrationRequest request,
            HttpServletRequest servletRequest
            ){
        RegistrationResponse response = authenticationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                factory.created(
                        response,
                        REGISTER_SUCCESS,
                        servletRequest.getRequestURI()
                )
        );
    }
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
