package com.nk.notification_system.service;

import com.nk.notification_system.dto.SubscriberData;
import com.nk.notification_system.message.NotificationTaskMessage;
import com.nk.notification_system.messaging.NotificationTaskProducer;
import com.nk.notification_system.model.Event;
import com.nk.notification_system.repository.EventRepository;
import org.springframework.stereotype.Service;

@Service
public class FanoutService {
    private final EventRepository eventRepository;
    private final SubscriptionService subscriptionService;
    private final NotificationRoutingService routingService;
    private final NotificationTaskProducer taskProducer;

    public FanoutService(EventRepository eventRepository,
                         SubscriptionService subscriptionService,
                         NotificationRoutingService routingService,
                         NotificationTaskProducer taskProducer) {
        this.eventRepository = eventRepository;
        this.subscriptionService = subscriptionService;
        this.routingService = routingService;
        this.taskProducer = taskProducer;
    }

    public void fanOut(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));

        for (SubscriberData subscriber : subscriptionService.findSubsToNotify(event.getEventName())) {
            var channel = routingService.resolve(event.getEventType(), subscriber.channel());
            taskProducer.publish(new NotificationTaskMessage(
                    event.getEventId(), subscriber.userId(), subscriber.userEmail(), channel,
                    event.getEventName(), event.getPayload(), 1));
        }

        System.out.println("Fanned out event " + eventId);
    }
}
