package com.nk.notification_system.dto;

import com.nk.notification_system.model.EventType;

import java.util.Date;

public record EventResponse(
        Long eventId,
        EventType eventType,
        String eventName,
        String payload,
        Date eventDate
) {
}
