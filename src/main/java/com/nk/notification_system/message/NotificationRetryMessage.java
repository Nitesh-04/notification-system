package com.nk.notification_system.message;

public record NotificationRetryMessage(Long eventId, Long userId, int attempt) {
}
