package com.nk.notification_system.repository;

import com.nk.notification_system.dto.SubscriberData;
import com.nk.notification_system.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByEventName(String eventName);

    boolean existsByUserUserIdAndEventName(Long userId, String eventName);

    @Query("""
    select new com.nk.notification_system.dto.SubscriberData(
        s.user.userId,
        s.user.email
    )
    from Subscription s
    where s.eventName = :eventName
""")
    List<SubscriberData> findEventSubscribers(String eventName);
}
