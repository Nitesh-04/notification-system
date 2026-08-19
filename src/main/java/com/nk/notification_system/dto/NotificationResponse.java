package com.nk.notification_system.dto;

import com.nk.notification_system.model.StatusType;

import java.util.Date;

public record NotificationResponse(
        Long notificationId,
        Long userId,
        Long eventId,
        StatusType status,
        Date sentAtDate
) {
}
