package com.nk.notification_system.service;

import com.nk.notification_system.messaging.NotificationRetryProducer;
import com.nk.notification_system.model.Event;
import com.nk.notification_system.model.Notification;
import com.nk.notification_system.model.StatusType;
import com.nk.notification_system.model.User;
import com.nk.notification_system.repository.EventRepository;
import jakarta.persistence.EntityManager;
import com.nk.notification_system.repository.NotificationRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationSendService {

    private final NotificationRepository notificationRepository;
    private final EntityManager entityManager;
    private final EventRepository eventRepository;
    private final NotificationRetryProducer notificationRetryProducer;
    private final MailService mailService;

    public NotificationSendService(NotificationRepository notificationRepository, EntityManager entityManager, EventRepository eventRepository, NotificationRetryProducer notificationRetryProducer, MailService mailService) {
        this.notificationRepository = notificationRepository;
        this.entityManager = entityManager;
        this.eventRepository = eventRepository;
        this.notificationRetryProducer = notificationRetryProducer;
        this.mailService = mailService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void notifyUser(Event event, Long userId, String userEmail) {

        if(userId == 5) {
            throw new RuntimeException("error"); // test to fail
        }

        mailService.sendEmail(userEmail, event.getEventName(), event.getPayload());

        Notification notification = new Notification();

        notification.setEvent(event);

        User userReference =
                entityManager.getReference(User.class, userId); // to avoid fetching full user to attach to notif
        notification.setUser(userReference);

        notification.setStatus(StatusType.SENT);

        notificationRepository.save(notification);

        System.out.println("Notification sent to " + userId + " " + userEmail + " - " + event.getPayload());
    }

    @Transactional
    public void retryNotifyUser(Long eventId, Long userId, String userEmail, int attempt) {
        try {
            Event event = eventRepository.findById(eventId).orElseThrow();

//            if(userId == 10) {
//                throw new RuntimeException("error");
//            }

            mailService.sendEmail(userEmail, event.getEventName(), event.getPayload());

            Notification notification = new Notification();
            notification.setEvent(event);

            User userReference =
                    entityManager.getReference(User.class, userId);
            notification.setUser(userReference);

            notification.setStatus(StatusType.RETRY_SENT);
            notificationRepository.save(notification);

            System.out.println("Notification sent to " + userId + " for event " + eventId + " - " + event.getPayload());
        } catch (DataIntegrityViolationException e) {
            System.out.println(
                    "Notification already sent"
            );
        } catch (Exception exception) {
            // kafka retry queue
            // subscription.getUser().getUserId() and event.getEventId
            System.out.println(
                    "Attempt " + attempt +
                            " - Notification retry failed for user " +
                            userId +
                            ", event " + eventId
            );
            notificationRetryProducer.publishRetry(eventId, userId, userEmail, attempt+1);
        }
    }
}
