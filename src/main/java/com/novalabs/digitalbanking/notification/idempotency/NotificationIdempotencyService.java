package com.novalabs.digitalbanking.notification.idempotency;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationIdempotencyService {

    private final NotificationIdempotencyRepository repository;

    @Transactional
    public boolean tryClaim(String notificationKey, String paymentReference, String notificationType, String channel) {
        NotificationIdempotency existing =
                repository.findByNotificationKey(notificationKey)
                        .orElse(null);

        if (existing != null) {
            return handleExisting(existing);
        }

        NotificationIdempotency record =
                new NotificationIdempotency(
                        notificationKey, paymentReference, notificationType, channel);

        try {
            repository.saveAndFlush(record);
            return true;
        } catch (DataIntegrityViolationException exception) {
            NotificationIdempotency concurrentRecord =
                    repository.findByNotificationKey(notificationKey)
                            .orElseThrow(() -> exception);
            return handleExisting(concurrentRecord);
        }
    }

    @Transactional
    public void markCompleted(String notificationKey) {
        NotificationIdempotency record =
                repository.findByNotificationKey(notificationKey)
                        .orElseThrow(() -> new IllegalStateException(
                                "Notification idempotency record not found : " + notificationKey
                        ));
        record.markCompleted();
        repository.save(record);
    }

    @Transactional
    public void markFailed(String notificationKey) {
        NotificationIdempotency record =
                repository.findByNotificationKey(notificationKey)
                        .orElseThrow(() -> new IllegalStateException(
                                "Notification idempotency record not found : " + notificationKey
                        ));
        record.markFailed();
        repository.save(record);
    }

    private boolean handleExisting(NotificationIdempotency existing) {
        if (existing.getStatus() == NotificationIdempotencyStatus.COMPLETED) {
            return false;
        }

        if (existing.getStatus() == NotificationIdempotencyStatus.PROCESSING) {
            return false;
        }

        if (existing.getStatus() == NotificationIdempotencyStatus.FAILED) {
            existing.markCompleted();
            repository.save(existing);
            return true;

        }
        return false;
    }
}
