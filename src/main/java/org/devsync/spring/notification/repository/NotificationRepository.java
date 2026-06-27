package org.devsync.spring.notification.repository;

import org.devsync.spring.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    Page<Notification> findByRecipientIdOrderByCreatedAtDesc(UUID recipientId, Pageable pageable);

    List<Notification> findByRecipientIdAndIsReadIsFalseOrderByCreatedAtAsc(UUID recipientId);


    long countByRecipientIdAndIsReadFalse(UUID recipientId);

    Page<Notification> findByWorkspaceIdAndRecipientIdOrderByCreatedAtDesc(UUID workspaceId, UUID recipientId,
                                                                           Pageable pageable);

    Page<Notification> findByProjectIdAndRecipientIdOrderByCreatedAtDesc(UUID projectId, UUID recipientId,
                                                                         Pageable pageable);

    @Modifying(flushAutomatically = true,clearAutomatically = true)
    @Query(""" 
        Update Notification n set n.isRead = true,n.readAt = :now
        where n.recipient.id = :userId and n.isRead = false
""")
    int markAllRead(@Param("userId") UUID userId, @Param("now")Instant now);
}
