package com.nk.notification_system.repository;

import com.nk.notification_system.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByEventName(String eventName);

    boolean existsByUserUserIdAndEventName(Long userId, String eventName);
}
