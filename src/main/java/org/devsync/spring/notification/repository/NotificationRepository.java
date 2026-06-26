package org.devsync.spring.notification.repository;

import org.devsync.spring.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    Page<Notification> findByRecipientIdOrderByCreatedAtDesc(UUID recipientId, Pageable pageable);



    long countByRecipientIdAndIsReadFalse(UUID recipientId);

    Page<Notification> findByWorkspaceIdAndRecipientIdOrderByCreatedAtDesc(UUID workspaceId, UUID recipientId,
                                                                           Pageable pageable);

    Page<Notification> findByProjectIdAndRecipientIdOrderByCreatedAtDesc(UUID projectId, UUID recipientId,
                                                                         Pageable pageable);
}
