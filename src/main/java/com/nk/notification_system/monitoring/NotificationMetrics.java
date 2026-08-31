package com.nk.notification_system.monitoring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class NotificationMetrics {

    private final Counter eventsReceived;
    private final Counter notificationsSent;
    private final Counter notificationsRetried;
    private final Counter notificationsFailed;
    private final Timer emailDeliveryTime;
    private final Timer eventNotificationProcessingTime;

    public NotificationMetrics(MeterRegistry meterRegistry) {
        this.eventsReceived = Counter.builder("events.received")
                .description("Number of events received by the consumer")
                .register(meterRegistry);

        this.notificationsSent = Counter.builder("notifications.sent")
                .description("Number of notifications sent successfully")
                .register(meterRegistry);

        this.notificationsFailed = Counter.builder("notifications.failed")
                .description("Number of failed notification attempts")
                .register(meterRegistry);

        this.notificationsRetried = Counter.builder("notifications.retried")
                .description("Number of notifications sent to retry")
                .register(meterRegistry);

        this.emailDeliveryTime = Timer.builder("email.delivery.time")
                .description("Time taken to send an email")
                .register(meterRegistry);

        this.eventNotificationProcessingTime =
                Timer.builder("event.notification.processing.time")
                        .description("Time taken to process notifications for an event")
                        .register(meterRegistry);
    }

    public void eventReceived() {
        eventsReceived.increment();
    }

    public void notificationSent() {
        notificationsSent.increment();
    }

    public void notificationFailed() {
        notificationsFailed.increment();
    }

    public void notificationRetried() {
        notificationsRetried.increment();
    }

    public Timer.Sample startEmailTimer() {
        return Timer.start();
    }

    public void stopEmailTimer(Timer.Sample sample) {
        sample.stop(emailDeliveryTime);
    }

    public Timer.Sample startEventNotificationProcessingTimer() {
        return Timer.start();
    }

    public void stopEventNotificationProcessingTimer(Timer.Sample sample) {
        sample.stop(eventNotificationProcessingTime);
    }

    public double eventsReceived() {
        return eventsReceived.count();
    }

    public double notificationsSent() {
        return notificationsSent.count();
    }

    public double notificationsRetried() {
        return notificationsRetried.count();
    }

    public double notificationsFailed() {
        return notificationsFailed.count();
    }

    public double averageEmailDeliveryTimeMs() {
        return emailDeliveryTime.mean(TimeUnit.MILLISECONDS);
    }

    public double averageEventProcessingTimeMs() {
        return eventNotificationProcessingTime.mean(TimeUnit.MILLISECONDS);
    }
}
