package com.nk.notification_system.repository;

import com.nk.notification_system.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByEventEventId(Long eventId);
}
