package com.nk.notification_system.model;

import jakarta.persistence.*;

//Subscription → @ManyToOne User
//Notification → @ManyToOne User
//Notification → @ManyToOne Event

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_subscription_user_event",
                        columnNames = {"user_id", "event_name"}
                )
        }
)
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_id")
    private Long subId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String eventName;

    public Long getSubId() {
        return subId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}
