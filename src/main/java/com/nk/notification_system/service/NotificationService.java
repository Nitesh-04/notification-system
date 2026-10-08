package com.nk.notification_system.service;

import com.nk.notification_system.dto.NotificationResponse;
import com.nk.notification_system.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<NotificationResponse> getNotificationsForEvent(Long eventId) {
        return notificationRepository.findByEventEventId(eventId)
                .stream()
                .map(notification -> new NotificationResponse(
                        notification.getNotificationId(),
                        notification.getUser().getUserId(),
                        notification.getEvent().getEventId(),
                        notification.getStatus(),
                        notification.getSentAtDate()
                ))
                .toList();
    }
}
