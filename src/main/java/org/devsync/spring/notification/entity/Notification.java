package org.devsync.spring.notification.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.devsync.spring.auth.entity.User;
import org.devsync.spring.common.entity.BaseEntity;

import java.time.Instant;
import java.util.UUID;

@Entity
@Setter
@Getter
@Table(
        indexes = {
                @Index(name = "idx_notification_recipient",
                        columnList = "recipient_id"),
                @Index(name = "idx_notification_recipient_read",
                        columnList = "recipient_id, is_read"),
                @Index(
                        name = "idx_notification_recipient_created",
                        columnList = "recipient_id, created_at"
                )
        }
)
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "recipient_id",nullable = false)
    private User recipient;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(nullable = false,length = 100)
    private String title;

    @Column(nullable = false,length = 500)
    private String message;

    @Column(nullable = false)
    private UUID resourceId;

    @Column(nullable = false)
    private boolean isRead = false;

    private Instant readAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResourceType resourceType;

    @Column(nullable = false)
    private UUID workspaceId;

    @Column(nullable = false)
    private UUID projectId;

}
