package com.nk.notification_system.messaging;

import com.nk.notification_system.message.NotificationTaskMessage;
import com.nk.notification_system.model.NotificationChannel;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationTaskProducer {
    public static final String EMAIL_TOPIC = "v3-email-notifications";
    public static final String PUSH_TOPIC = "v3-push-notifications";

    private final KafkaTemplate<String, NotificationTaskMessage> kafkaTemplate;

    public NotificationTaskProducer(KafkaTemplate<String, NotificationTaskMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(NotificationTaskMessage task) {
        String topic = task.channel() == NotificationChannel.EMAIL ? EMAIL_TOPIC : PUSH_TOPIC;
        String key = task.eventId() + ":" + task.userId() + ":" + task.channel();
        kafkaTemplate.send(topic, key, task);
    }
}
