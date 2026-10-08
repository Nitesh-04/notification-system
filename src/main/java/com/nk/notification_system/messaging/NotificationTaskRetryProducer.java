package com.nk.notification_system.messaging;

import com.nk.notification_system.message.NotificationTaskMessage;
import com.nk.notification_system.model.NotificationChannel;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationTaskRetryProducer {
    private static final int MAX_ATTEMPTS = 3;
    private final KafkaTemplate<String, NotificationTaskMessage> kafkaTemplate;

    public NotificationTaskRetryProducer(KafkaTemplate<String, NotificationTaskMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public boolean retryOrDeadLetter(NotificationTaskMessage task) {
        String topic = task.channel() == NotificationChannel.EMAIL
                ? NotificationTaskProducer.EMAIL_TOPIC
                : NotificationTaskProducer.PUSH_TOPIC;
        if (task.attempt() >= MAX_ATTEMPTS) {
            kafkaTemplate.send(topic + ".dlq", key(task), task);
            return false;
        }
        NotificationTaskMessage retry = new NotificationTaskMessage(
                task.eventId(), task.userId(), task.userEmail(), task.channel(),
                task.eventName(), task.payload(), task.attempt() + 1);
        kafkaTemplate.send(topic, key(retry), retry);
        return true;
    }

    private String key(NotificationTaskMessage task) {
        return task.eventId() + ":" + task.userId() + ":" + task.channel();
    }
}
