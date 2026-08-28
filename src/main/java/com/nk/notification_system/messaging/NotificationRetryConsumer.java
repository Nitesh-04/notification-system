package com.nk.notification_system.messaging;

import com.nk.notification_system.message.NotificationRetryMessage;
import com.nk.notification_system.service.NotificationFailService;
import com.nk.notification_system.service.NotificationSendService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class NotificationRetryConsumer {

    private final ObjectMapper objectMapper;
    private final NotificationSendService notificationSendService;

    public NotificationRetryConsumer(ObjectMapper objectMapper, NotificationSendService notificationSendService) {
        this.objectMapper = objectMapper;
        this.notificationSendService = notificationSendService;
    }

    @KafkaListener(
            topics = "notification-retry",
            groupId = "notification-workers"
    )
    public void consume(String jsonMessage) throws Exception {

        NotificationRetryMessage notificationRetryMessage = objectMapper.readValue(jsonMessage, NotificationRetryMessage.class);

        System.out.println("Received retry event " + notificationRetryMessage.eventId() + " attempt " + notificationRetryMessage.attempt());

        notificationSendService.retryNotifyUser(notificationRetryMessage.eventId(), notificationRetryMessage.userId(), notificationRetryMessage.attempt());
    }
}
