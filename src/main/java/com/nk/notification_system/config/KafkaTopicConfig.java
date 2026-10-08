package com.nk.notification_system.config;

import com.nk.notification_system.messaging.NotificationTaskProducer;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {
    private static final int PARTITIONS = 3;

    @Bean
    NewTopic emailTasksTopic() {
        return new NewTopic(NotificationTaskProducer.EMAIL_TOPIC, PARTITIONS, (short) 1);
    }

    @Bean
    NewTopic pushTasksTopic() {
        return new NewTopic(NotificationTaskProducer.PUSH_TOPIC, PARTITIONS, (short) 1);
    }

    @Bean
    NewTopic emailDlqTopic() {
        return new NewTopic(NotificationTaskProducer.EMAIL_TOPIC + ".dlq", PARTITIONS, (short) 1);
    }

    @Bean
    NewTopic pushDlqTopic() {
        return new NewTopic(NotificationTaskProducer.PUSH_TOPIC + ".dlq", PARTITIONS, (short) 1);
    }
}
