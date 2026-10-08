package com.nk.notification_system.messaging;

import com.nk.notification_system.message.NotificationTaskMessage;
import com.nk.notification_system.service.NotificationTaskService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class EmailTaskConsumer {
    private final ObjectMapper objectMapper;
    private final NotificationTaskService taskService;
    private final NotificationTaskRetryProducer retryProducer;

    public EmailTaskConsumer(ObjectMapper objectMapper, NotificationTaskService taskService,
                             NotificationTaskRetryProducer retryProducer) {
        this.objectMapper = objectMapper;
        this.taskService = taskService;
        this.retryProducer = retryProducer;
    }

    @KafkaListener(topics = NotificationTaskProducer.EMAIL_TOPIC,
            groupId = "v3-email-workers", concurrency = "${notification.email-worker-concurrency:3}")
    public void consume(String json) throws Exception {
        process(objectMapper.readValue(json, NotificationTaskMessage.class));
    }

    private void process(NotificationTaskMessage task) {
        try {
            taskService.deliver(task);
        } catch (Exception exception) {
            System.out.println("Email task failed, attempt " + task.attempt() + " for event " + task.eventId() + ", user " + task.userId());
            if (!retryProducer.retryOrDeadLetter(task)) {
                taskService.markFailed(task);
                System.out.println("Email task moved to DLQ for event " + task.eventId() + ", user " + task.userId());
            }
        }
    }
}
