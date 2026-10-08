package com.nk.notification_system.service;

import com.nk.notification_system.model.NotificationChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.EnumMap;

@Service
public class ChannelRateLimiter {
    private final EnumMap<NotificationChannel, Long> nextAllowed = new EnumMap<>(NotificationChannel.class);
    private final long intervalMillis;

    public ChannelRateLimiter(@Value("${notification.rate-limit-per-second:20}") int perSecond) {
        this.intervalMillis = Math.max(1, 1000L / Math.max(1, perSecond));
    }

    public synchronized void acquire(NotificationChannel channel) {
        long now = System.currentTimeMillis();
        long allowed = nextAllowed.getOrDefault(channel, now);
        if (allowed > now) {
            try {
                Thread.sleep(allowed - now);
            } catch (InterruptedException interrupted) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Rate limiter interrupted", interrupted);
            }
        }
        nextAllowed.put(channel, Math.max(now, allowed) + intervalMillis);
    }
}
