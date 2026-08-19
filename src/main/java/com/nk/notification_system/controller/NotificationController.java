package com.nk.notification_system.controller;

import com.nk.notification_system.dto.NotificationResponse;
import com.nk.notification_system.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<NotificationResponse>> getNotifications(@PathVariable Long eventId) {
        List<NotificationResponse> notifications = notificationService.getNotificationsForEvent(eventId);

        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }
}
