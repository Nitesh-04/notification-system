package com.nk.notification_system.service;

import com.nk.notification_system.dto.CreateEventDTO;
import com.nk.notification_system.dto.EventResponse;
import com.nk.notification_system.model.Event;
import com.nk.notification_system.model.Subscription;
import com.nk.notification_system.repository.EventRepository;
import com.nk.notification_system.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final NotificationService notificationService;

    public EventService(EventRepository eventRepository, SubscriptionRepository subscriptionRepository, NotificationService notificationService) {
        this.eventRepository = eventRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.notificationService = notificationService;
    }

    public List<EventResponse> findAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public Event createEvent(CreateEventDTO createEventDTO) {
        Event event = new Event();
        event.setEventType(createEventDTO.eventType());
        event.setEventName(createEventDTO.eventName());
        event.setPayload(createEventDTO.payload());

        Event savedEvent = eventRepository.save(event);

        List<Subscription> subscribers = findSubsToNotify(savedEvent.getEventName());

        notificationService.sendNotifications(savedEvent, subscribers);

        return savedEvent;
    }

    public EventResponse toResponse(Event event) {
        return new EventResponse(
                event.getEventId(),
                event.getEventType(),
                event.getEventName(),
                event.getPayload(),
                event.getEventDate()
        );
    }

    public List<Subscription> findSubsToNotify(String eventName) {
        return subscriptionRepository.findByEventName(eventName);
    }
}
