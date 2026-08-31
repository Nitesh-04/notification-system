package com.nk.notification_system.service;

import com.nk.notification_system.dto.StatsResponse;
import com.nk.notification_system.monitoring.NotificationMetrics;
import org.springframework.stereotype.Service;

@Service
public class StatsService {

    private final NotificationMetrics metrics;

    public StatsService(NotificationMetrics metrics) {
        this.metrics = metrics;
    }

    public StatsResponse getSummary() {
        double events = metrics.eventsReceived();

        double averageNotifications =
                events == 0
                        ? 0
                        : metrics.notificationsSent() / events;

        double averageRetries =
                events == 0
                        ? 0
                        : metrics.notificationsRetried() / events;

        return new StatsResponse(
                events,
                metrics.notificationsSent(),
                metrics.notificationsRetried(),
                metrics.notificationsFailed(),
                averageNotifications,
                averageRetries,
                metrics.averageEmailDeliveryTimeMs(),
                metrics.averageEventProcessingTimeMs()
        );
    }
}
