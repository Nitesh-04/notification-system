package com.nk.notification_system.dto;

import com.nk.notification_system.model.NotificationChannel;

public record SubscriberData(Long userId, String userEmail, NotificationChannel channel) {
}
