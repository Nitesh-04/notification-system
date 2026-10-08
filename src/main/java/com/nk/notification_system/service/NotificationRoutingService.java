package com.nk.notification_system.service;

import com.nk.notification_system.model.EventType;
import com.nk.notification_system.model.NotificationChannel;
import org.springframework.stereotype.Service;

@Service
public class NotificationRoutingService {

    public NotificationChannel defaultChannel(EventType eventType) {
        return eventType == EventType.PROMO
                ? NotificationChannel.EMAIL
                : NotificationChannel.PUSH;
    }

    public NotificationChannel resolve(EventType eventType, NotificationChannel preference) {
        return preference == null ? defaultChannel(eventType) : preference;
    }
}
