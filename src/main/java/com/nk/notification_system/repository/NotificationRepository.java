package com.nk.notification_system.repository;

import com.nk.notification_system.model.Notification;
import com.nk.notification_system.model.NotificationChannel;
import com.nk.notification_system.model.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByEventEventId(Long eventId);

    @Modifying
    @Query(value = """
            INSERT INTO notification (event_id, user_id, channel, status, sent_at_date)
            VALUES (:eventId, :userId, :channel, 'PENDING', CURRENT_TIMESTAMP)
            ON CONFLICT DO NOTHING
            """, nativeQuery = true)
    int claimTask(@Param("eventId") Long eventId,
                  @Param("userId") Long userId,
                  @Param("channel") String channel);

    @Modifying
    @Query("""
            update Notification n
            set n.status = :status
            where n.event.eventId = :eventId
              and n.user.userId = :userId
              and n.channel = :channel
            """)
    int updateTaskStatus(@Param("eventId") Long eventId,
                         @Param("userId") Long userId,
                         @Param("channel") NotificationChannel channel,
                         @Param("status") StatusType status);
}
