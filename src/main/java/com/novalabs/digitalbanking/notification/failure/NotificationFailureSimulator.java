package com.novalabs.digitalbanking.notification.failure;


import com.novalabs.digitalbanking.common.exception.NotificationDeliveryException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Profile({"dev","test"})
public class NotificationFailureSimulator {

    private final boolean enabled;
    private final int failUntilAttempt;

    private final ConcurrentHashMap<String, AtomicInteger> attempts =
            new ConcurrentHashMap<>();

    public NotificationFailureSimulator(
            @Value("${banking.notification.failure-simulation.enabled:false}")
            boolean enabled,
            @Value("${banking.notification.failure-simulation.fail-until-attempt:0}")
            int failUntilAttempt
    ){
        this.enabled = enabled;
        this.failUntilAttempt = failUntilAttempt;
    }

    public void beforeSend(String paymentReference){
        if (!enabled){
            return;
        }

        int attempt = attempts.computeIfAbsent(
                paymentReference,
                key -> new AtomicInteger()
        )
                .incrementAndGet();
        if (attempt <= failUntilAttempt){
            throw new NotificationDeliveryException("Simulate notification provider failure. " +
                    "paymentReference=" + paymentReference +", attempt="+attempt);
        }

    }


}
