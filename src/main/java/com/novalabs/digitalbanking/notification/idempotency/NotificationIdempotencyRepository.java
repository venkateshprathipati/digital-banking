package com.novalabs.digitalbanking.notification.idempotency;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationIdempotencyRepository extends JpaRepository<NotificationIdempotency, Long> {

    Optional<NotificationIdempotency> findByNotificationKey(String notificationKey);
}
