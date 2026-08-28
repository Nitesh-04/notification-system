package com.nk.notification_system.service;

import com.nk.notification_system.dto.NotificationResponse;
import com.nk.notification_system.dto.SubscriberData;
import com.nk.notification_system.messaging.NotificationRetryProducer;
import com.nk.notification_system.model.*;
import com.nk.notification_system.repository.EventRepository;
import com.nk.notification_system.repository.NotificationRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EventRepository eventRepository;
    private final SubscriptionService subscriptionService;
    private final NotificationSendService notificationSendService;
    private final NotificationRetryProducer notificationRetryProducer;

    public NotificationService(NotificationRepository notificationRepository, EventRepository eventRepository, SubscriptionService subscriptionService, NotificationSendService notificationSendService, NotificationRetryProducer notificationRetryProducer) {
        this.notificationRepository = notificationRepository;
        this.eventRepository = eventRepository;
        this.subscriptionService = subscriptionService;
        this.notificationSendService = notificationSendService;
        this.notificationRetryProducer = notificationRetryProducer;
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

    public void sendNotifications(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found: " + eventId));

        List<SubscriberData> subscriptions = subscriptionService.findSubsToNotify(event.getEventName());

        for (SubscriberData subscription : subscriptions) {
            try {
                notificationSendService.notifyUser(
                        event,
                        subscription.userId(),
                        subscription.userEmail()
                );
            } catch (DataIntegrityViolationException e) {
                System.out.println(
                        "Notification already sent"
                );
            } catch (Exception exception) {
                // kafka retry queue
                // subscription.getUser().getUserId() and event.getEventId
                System.out.println(
                        "Notification failed for user " +
                                subscription.userId() +
                                ", event " + eventId
                );
                notificationRetryProducer.publishRetry(event.getEventId(), subscription.userId(), 1);
            }
        }
    }
}
