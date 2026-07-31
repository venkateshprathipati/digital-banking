package com.novalabs.digitalbanking.identity.service.impl;

import com.novalabs.digitalbanking.common.exception.ErrorCode;
import com.novalabs.digitalbanking.common.exception.UserAlreadyExistsException;
import com.novalabs.digitalbanking.identity.dto.LoginRequest;
import com.novalabs.digitalbanking.identity.dto.LoginResponse;
import com.novalabs.digitalbanking.identity.dto.RegistrationRequest;
import com.novalabs.digitalbanking.identity.dto.RegistrationResponse;
import com.novalabs.digitalbanking.identity.entity.Role;
import com.novalabs.digitalbanking.identity.entity.User;
import com.novalabs.digitalbanking.identity.mapper.UserMapper;
import com.novalabs.digitalbanking.identity.repository.UserRepository;
import com.novalabs.digitalbanking.identity.security.UserPrincipal;
import com.novalabs.digitalbanking.identity.service.AuthenticationService;
import com.novalabs.digitalbanking.security.jwt.JwtProperties;
import com.novalabs.digitalbanking.security.jwt.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService  jwtService;
    private final JwtProperties properties;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public RegistrationResponse register(RegistrationRequest request) {
        String username = request.username().trim();
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException(
                    ErrorCode.USERNAME_ALREADY_EXISTS,
                    "Username is already registered"
            );
        }
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException(
                    ErrorCode.EMAIL_ALREADY_EXISTS,
                    "Email is already registered"
            );
        }

        User user = userMapper.toEntity(request);
        user.setUsername(username);
        user.setEmail(email);

        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.CUSTOMER);
        user.setEnabled(true);
        user.setAccountNonLocked(true);

        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

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
