package com.nk.notification_system.dto;

public record SubscriptionResponse(
        Long subId,
        Long userId,
        String eventName
) {
}
