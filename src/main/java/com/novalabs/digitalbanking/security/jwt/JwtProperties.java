package com.novalabs.digitalbanking.security.jwt;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {
    /**
     * Secret key used to sign JWT tokens.
     * Must be at least 32 characters (256 bits) for HS256.
     */
    @NotBlank
    private String secret;
    /**
     * Access token expiration time in milliseconds.
     * Example:
     * 15 minutes = 900000
     */
    @Min(60000)
    private long expiration;
    /**
     * Token issuer.
     */
    @NotBlank
    private String issuer;


}
