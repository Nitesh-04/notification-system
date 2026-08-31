package com.nk.notification_system.messaging;

import com.nk.notification_system.message.EventCreateMessage;
import com.nk.notification_system.monitoring.NotificationMetrics;
import com.nk.notification_system.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class EventConsumer {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;
    private final NotificationMetrics notificationMetrics;

    public EventConsumer(NotificationService notificationService, ObjectMapper objectMapper, NotificationMetrics notificationMetrics) {
        this.notificationService = notificationService;
        this.objectMapper = objectMapper;
        this.notificationMetrics = notificationMetrics;
    }

    @KafkaListener(
            topics = "event-created",
            groupId = "notification-workers"
    )
    public void consume(String jsonMessage) throws Exception {

        EventCreateMessage eventCreateMessage = objectMapper.readValue(jsonMessage, EventCreateMessage.class);

        System.out.println("Received event " + eventCreateMessage.eventId());

        notificationMetrics.eventReceived();

        notificationService.sendNotifications(eventCreateMessage.eventId());
    }
}
