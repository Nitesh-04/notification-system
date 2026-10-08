package com.nk.notification_system.message;

import com.nk.notification_system.model.NotificationChannel;

public record NotificationTaskMessage(
        Long eventId,
        Long userId,
        String userEmail,
        NotificationChannel channel,
        String eventName,
        String payload,
        int attempt
) {
}
