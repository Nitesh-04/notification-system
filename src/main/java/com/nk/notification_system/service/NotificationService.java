package com.nk.notification_system.service;

import com.nk.notification_system.dto.NotificationResponse;
import com.nk.notification_system.model.Event;
import com.nk.notification_system.model.Notification;
import com.nk.notification_system.model.StatusType;
import com.nk.notification_system.model.Subscription;
import com.nk.notification_system.repository.EventRepository;
import com.nk.notification_system.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EventRepository eventRepository;
    private final SubscriptionService subscriptionService;

    public NotificationService(NotificationRepository notificationRepository, EventRepository eventRepository, SubscriptionService subscriptionService) {
        this.notificationRepository = notificationRepository;
        this.eventRepository = eventRepository;
        this.subscriptionService = subscriptionService;
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

    @Transactional
    public void sendNotification(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found: " + eventId));

        List<Subscription> subscriptions = subscriptionService.findSubsToNotify(event.getEventName());

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
