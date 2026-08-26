package com.novalabs.digitalbanking.common.redis;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/v1/redis/test")
@RequiredArgsConstructor
@Validated
public class RedisTestController {
    private final RedisService redisService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> set(
            @RequestParam @NotBlank String key,
            @RequestParam @NotBlank String value,
            @RequestParam @NotNull @Positive Long ttlSeconds
    ) {

        redisService.set(key, value, Duration.ofSeconds(ttlSeconds));

        return ResponseEntity.ok(
                Map.of("key", key, "status", "stored", "ttlSeconds", ttlSeconds)
        );
    }

    @GetMapping("/{key}")
    public ResponseEntity<Map<String, Object>> get(@PathVariable @NotBlank String key) {
        return redisService.get(key)
                .map(value -> ResponseEntity.ok(
                        Map.<String, Object>of(
                                "key", key,
                                "value", value
                        )
                ))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable @NotBlank String key) {
        boolean deleted = redisService.delete(key);
        return ResponseEntity.ok(
                Map.of(
                        "key", key,
                        "deleted", deleted
                )
        );
    }
}
