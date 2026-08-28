package com.nk.notification_system.messaging;

import com.nk.notification_system.message.NotificationRetryMessage;
import com.nk.notification_system.service.NotificationFailService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationRetryProducer {
    private static final String TOPIC = "notification-retry";

    private final KafkaTemplate<String, NotificationRetryMessage> kafkaTemplate;
    private final NotificationFailService notificationFailService;

    public NotificationRetryProducer(KafkaTemplate<String, NotificationRetryMessage> kafkaTemplate, NotificationFailService notificationFailService) {
        this.kafkaTemplate = kafkaTemplate;
        this.notificationFailService = notificationFailService;
    }

    public void publishRetry(Long eventId, Long userId, String userEmail, Integer attempt) {
        NotificationRetryMessage notificationRetryMessage = new NotificationRetryMessage(eventId, userId, userEmail, attempt);

        if(attempt == 4) {
            notificationFailService.markAsFailNotification(eventId, userId);
            return;
        }

        String key = eventId + ":" + userId;

        kafkaTemplate.send(
                TOPIC,
                key,
                notificationRetryMessage
        );
    }
}
