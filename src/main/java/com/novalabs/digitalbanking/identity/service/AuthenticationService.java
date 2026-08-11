package com.novalabs.digitalbanking.identity.service;

import com.novalabs.digitalbanking.identity.dto.LoginRequest;
import com.novalabs.digitalbanking.identity.dto.LoginResponse;
import com.novalabs.digitalbanking.identity.dto.RegistrationRequest;
import com.novalabs.digitalbanking.identity.dto.RegistrationResponse;

public interface AuthenticationService {

    RegistrationResponse register(RegistrationRequest request);

    LoginResponse authenticate(LoginRequest loginRequest);
}
