package com.nk.notification_system.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(
        name = "notification",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unq_notification_user_event",  // to ensure no duplication notification is sent (idempotency)
                        columnNames = {"user_id", "event_id", "channel"}
                )
        }
)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Enumerated(EnumType.STRING)
    private StatusType status;

    @Enumerated(EnumType.STRING)
    private NotificationChannel channel;

    @CreationTimestamp
    private Date sentAtDate;

    public Long getNotificationId() {
        return notificationId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public NotificationChannel getChannel() {
        return channel;
    }

    public void setChannel(NotificationChannel channel) {
        this.channel = channel;
    }

    public Date getSentAtDate() {
        return sentAtDate;
    }

    public void setSentAtDate(Date sentAtDate) {
        this.sentAtDate = sentAtDate;
    }
}
