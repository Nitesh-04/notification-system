package com.nk.notification_system.messaging;

import com.nk.notification_system.message.EventCreateMessage;
import com.nk.notification_system.service.FanoutService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class EventConsumer {

    private final FanoutService fanoutService;
    private final ObjectMapper objectMapper;

    public EventConsumer(FanoutService fanoutService, ObjectMapper objectMapper) {
        this.fanoutService = fanoutService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "event-created",
            groupId = "notification-workers"
    )
    public void consume(String jsonMessage) throws Exception {

        EventCreateMessage eventCreateMessage = objectMapper.readValue(jsonMessage, EventCreateMessage.class);

        System.out.println("Received event " + eventCreateMessage.eventId());

        fanoutService.fanOut(eventCreateMessage.eventId());
    }
}
