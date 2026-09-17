package com.novalabs.digitalbanking.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "banking.cache")
public record CacheProperties(
        Duration accountTtl
) {
}
