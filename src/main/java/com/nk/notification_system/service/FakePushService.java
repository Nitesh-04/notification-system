package com.nk.notification_system.service;

import org.springframework.stereotype.Service;

@Service
public class FakePushService {
    public void sendPush(Long userId, String title, String payload) {
        System.out.println("Push sent to user " + userId + " - " + title + ": " + payload);
    }
}
