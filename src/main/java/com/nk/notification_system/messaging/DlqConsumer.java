package com.nk.notification_system.messaging;

import com.nk.notification_system.message.NotificationTaskMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class DlqConsumer {
    private final ObjectMapper objectMapper;

    public DlqConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = {
                    NotificationTaskProducer.EMAIL_TOPIC + ".dlq",
                    NotificationTaskProducer.PUSH_TOPIC + ".dlq"
            },
            groupId = "v3-dlq-watchers",
            concurrency = "1")
    public void consume(String json) throws Exception {
        NotificationTaskMessage task = objectMapper.readValue(json, NotificationTaskMessage.class);

        System.out.println("DLQ received: event " + task.eventId()
                + ", user " + task.userId()
                + " (" + task.userEmail() + ")"
                + ", channel " + task.channel()
                + ", eventName " + task.eventName()
                + ", attempt " + task.attempt()
                + ", payload " + task.payload());
    }
}
