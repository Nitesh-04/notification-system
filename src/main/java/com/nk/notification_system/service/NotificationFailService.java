package com.nk.notification_system.service;

import com.nk.notification_system.model.Event;
import com.nk.notification_system.model.Notification;
import com.nk.notification_system.model.StatusType;
import com.nk.notification_system.model.User;
import com.nk.notification_system.monitoring.NotificationMetrics;
import com.nk.notification_system.repository.NotificationRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationFailService {

    private final NotificationRepository notificationRepository;
    private final EntityManager entityManager;
    private final NotificationMetrics notificationMetrics;

    public NotificationFailService(NotificationRepository notificationRepository, EntityManager entityManager, NotificationMetrics notificationMetrics) {
        this.notificationRepository = notificationRepository;
        this.entityManager = entityManager;
        this.notificationMetrics = notificationMetrics;
    }

    @Transactional
    public void markAsFailNotification(Long eventId, Long userId) {
        Notification notification =  new Notification();
        User userReference =
                entityManager.getReference(User.class, userId); // to avoid fetching full user to attach to notif
        notification.setUser(userReference);

        Event eventReference = entityManager.getReference(Event.class, eventId);

        notification.setEvent(eventReference);
        notification.setStatus(StatusType.FAILED);
        notificationRepository.save(notification);

        notificationMetrics.notificationFailed();

        System.out.println("Notification failed for user " + userId + " with event " + eventId);
    }
}
