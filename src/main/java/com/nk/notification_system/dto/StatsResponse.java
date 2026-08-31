package com.nk.notification_system.dto;

public record StatsResponse(
        double eventsReceived,
        double notificationsSent,
        double notificationsRetried,
        double notificationsFailed,
        double averageNotificationsPerEvent,
        double averageRetriesPerEvent,
        double averageEmailDeliveryTimeMs,
        double averageEventProcessingTimeMs
) {
}
