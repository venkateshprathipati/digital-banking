package com.novalabs.digitalbanking.identity.service;

import com.novalabs.digitalbanking.identity.dto.LoginRequest;
import com.novalabs.digitalbanking.identity.dto.LoginResponse;

public interface AuthenticationService {

    LoginResponse authenticate(LoginRequest loginRequest);
}
