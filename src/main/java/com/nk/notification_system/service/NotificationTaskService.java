package com.nk.notification_system.service;

import com.nk.notification_system.message.NotificationTaskMessage;
import com.nk.notification_system.model.NotificationChannel;
import com.nk.notification_system.model.StatusType;
import com.nk.notification_system.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationTaskService {
    private final NotificationRepository notificationRepository;
    private final MailService mailService;
    private final FakePushService fakePushService;
    private final ChannelRateLimiter rateLimiter;

    public NotificationTaskService(NotificationRepository notificationRepository,
                                   MailService mailService,
                                   FakePushService fakePushService,
                                   ChannelRateLimiter rateLimiter) {
        this.notificationRepository = notificationRepository;
        this.mailService = mailService;
        this.fakePushService = fakePushService;
        this.rateLimiter = rateLimiter;
    }

    @Transactional
    public boolean deliver(NotificationTaskMessage task) {
        int claimed = notificationRepository.claimTask(
                task.eventId(), task.userId(), task.channel().name());
        if (claimed == 0) {
            System.out.println("Task already claimed or delivered: " + task.eventId() + ":" + task.userId() + ":" + task.channel());
            return false;
        }

        rateLimiter.acquire(task.channel());
        if (task.channel() == NotificationChannel.EMAIL) {
            mailService.sendEmail(task.userEmail(), task.eventName(), task.payload());
        } else {
            fakePushService.sendPush(task.userId(), task.eventName(), task.payload());
        }

        notificationRepository.updateTaskStatus(
                task.eventId(), task.userId(), task.channel(), StatusType.SENT);
        return true;
    }

    @Transactional
    public void markFailed(NotificationTaskMessage task) {
        // The delivery transaction rolls back the PENDING claim when the provider fails.
        // Re-claim it here so the final failure is still durable before it enters the DLQ.
        notificationRepository.claimTask(task.eventId(), task.userId(), task.channel().name());
        notificationRepository.updateTaskStatus(
                task.eventId(), task.userId(), task.channel(), StatusType.FAILED);
    }
}
