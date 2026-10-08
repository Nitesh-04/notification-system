package com.nk.notification_system.messaging;

import com.nk.notification_system.message.NotificationTaskMessage;
import com.nk.notification_system.service.NotificationTaskService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class PushTaskConsumer {
    private final ObjectMapper objectMapper;
    private final NotificationTaskService taskService;
    private final NotificationTaskRetryProducer retryProducer;

    public PushTaskConsumer(ObjectMapper objectMapper, NotificationTaskService taskService,
                            NotificationTaskRetryProducer retryProducer) {
        this.objectMapper = objectMapper;
        this.taskService = taskService;
        this.retryProducer = retryProducer;
    }

    @KafkaListener(topics = NotificationTaskProducer.PUSH_TOPIC,
            groupId = "v3-push-workers", concurrency = "${notification.push-worker-concurrency:3}")
    public void consume(String json) throws Exception {
        NotificationTaskMessage task = objectMapper.readValue(json, NotificationTaskMessage.class);
        try {
            taskService.deliver(task);
        } catch (Exception exception) {
            System.out.println("Push task failed, attempt " + task.attempt() + " for event " + task.eventId() + ", user " + task.userId());
            if (!retryProducer.retryOrDeadLetter(task)) {
                taskService.markFailed(task);
                System.out.println("Push task moved to DLQ for event " + task.eventId() + ", user " + task.userId());
            }
        }
    }
}
