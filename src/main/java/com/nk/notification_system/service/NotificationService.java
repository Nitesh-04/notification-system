package com.nk.notification_system.service;

import com.nk.notification_system.dto.NotificationResponse;
import com.nk.notification_system.model.Event;
import com.nk.notification_system.model.Notification;
import com.nk.notification_system.model.StatusType;
import com.nk.notification_system.model.Subscription;
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

    public void sendNotifications(Event event, List<Subscription> subscriptions) {
        for (Subscription subscription : subscriptions) {
            Notification notification = new Notification();

            notification.setUser(subscription.getUser());
            notification.setEvent(event);
            notification.setStatus(StatusType.SENT);

            Notification sentNotification = notificationRepository.save(notification);

            System.out.println("Sent to " + sentNotification.getUser().getEmail());
        }
    }
}
