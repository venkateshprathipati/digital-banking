package com.novalabs.digitalbanking.common.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisConnectivityService {

    private static final String HEALTH_CHECK_KEY = "platform:redis:health-check";
    private static final String HEALTH_CHECK_VALUE = "ok";

    private final RedisTemplate<String, String> redisTemplate;

    public boolean isAvailable() {
        try {
            redisTemplate.opsForValue().set(HEALTH_CHECK_KEY, HEALTH_CHECK_VALUE);
            return HEALTH_CHECK_VALUE.equals(redisTemplate.opsForValue().get(HEALTH_CHECK_KEY));
        } catch (RuntimeException ex) {
            return false;
        }
    }
}
