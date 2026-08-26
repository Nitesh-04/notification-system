package com.nk.notification_system.service;

import com.nk.notification_system.model.Event;
import com.nk.notification_system.model.Notification;
import com.nk.notification_system.model.StatusType;
import com.nk.notification_system.model.User;
import jakarta.persistence.EntityManager;
import com.nk.notification_system.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationSendService {

    private final NotificationRepository notificationRepository;
    private final EntityManager entityManager;

    public NotificationSendService(NotificationRepository notificationRepository, EntityManager entityManager) {
        this.notificationRepository = notificationRepository;
        this.entityManager = entityManager;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void  sendNotification(Event event, Long userId, String userEmail) {

        if(userId == 10) {
            throw new RuntimeException("error"); // test to fail
        }

        Notification notification = new Notification();

        notification.setEvent(event);

        User userReference =
                entityManager.getReference(User.class, userId); // to avoid fetching full user to attach to notif
        notification.setUser(userReference);

        notification.setStatus(StatusType.SENT);

        notificationRepository.save(notification);

        System.out.println("Notification sent to " + userId + " " + userEmail + " - " + event.getPayload());
    }
}
