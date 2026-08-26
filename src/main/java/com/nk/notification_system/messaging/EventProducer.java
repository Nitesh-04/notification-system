package com.nk.notification_system.messaging;

import com.nk.notification_system.message.EventCreateMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {
    private static final String TOPIC = "event-created";

    private final KafkaTemplate<String, EventCreateMessage> kafkaTemplate;

    public EventProducer(KafkaTemplate<String, EventCreateMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishEvent(Long eventId) {
        EventCreateMessage eventCreateMessage = new EventCreateMessage(eventId);

        kafkaTemplate.send(
                TOPIC,
                eventId.toString(),
                eventCreateMessage
        );
    }


}
