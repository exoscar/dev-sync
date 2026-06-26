package org.devsync.spring.notification.dto;

import lombok.Builder;
import org.devsync.spring.notification.entity.NotificationType;
import org.devsync.spring.notification.entity.ResourceType;

import java.time.Instant;
import java.util.UUID;

@Builder
public record NotificationResponse(
        UUID id,
        NotificationType type,
        String title,
        String message,
        ResourceType resourceType,
        UUID resourceId,
        boolean read,
        Instant readAt,
        Instant createdAt,
        UUID projectId,
        UUID workspaceId
) {
}
