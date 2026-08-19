package com.nk.notification_system.repository;

import com.nk.notification_system.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
