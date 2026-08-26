package com.nk.notification_system.messaging;

import com.nk.notification_system.message.EventCreateMessage;
import com.nk.notification_system.model.Subscription;
import com.nk.notification_system.service.EventService;
import com.nk.notification_system.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Component
public class EventConsumer {

    private final NotificationService notificationService;

    public EventConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(
            topics = "event-created",
            groupId = "notification-workers"
    )
    public void consume(String jsonMessage) throws Exception {

        EventCreateMessage eventCreateMessage = new ObjectMapper().readValue(jsonMessage, EventCreateMessage.class);

        System.out.println("Received event " + eventCreateMessage.eventId());

        notificationService.sendNotification(eventCreateMessage.eventId());
    }
}
