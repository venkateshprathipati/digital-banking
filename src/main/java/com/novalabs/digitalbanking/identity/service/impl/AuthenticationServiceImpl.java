package com.novalabs.digitalbanking.identity.service.impl;

import com.novalabs.digitalbanking.identity.dto.LoginRequest;
import com.novalabs.digitalbanking.identity.dto.LoginResponse;
import com.novalabs.digitalbanking.identity.security.UserPrincipal;
import com.novalabs.digitalbanking.identity.service.AuthenticationService;
import com.novalabs.digitalbanking.security.jwt.JwtProperties;
import com.novalabs.digitalbanking.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService  jwtService;
    private final JwtProperties properties;

    @Override
    public LoginResponse authenticate(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),loginRequest.password()
                )
        );

        UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();

        String token = jwtService.generateToken(userPrincipal);

        return new LoginResponse(
                token,"Bearer", properties.getExpiration() / 1000
        );
    }
}
