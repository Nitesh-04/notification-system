package com.nk.notification_system.dto;

import com.nk.notification_system.model.NotificationChannel;

public record SubscriptionResponse(
        Long subId,
        Long userId,
        String eventName,
        NotificationChannel channel
) {
}
