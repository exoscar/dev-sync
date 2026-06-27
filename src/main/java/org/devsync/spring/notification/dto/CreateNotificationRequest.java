package org.devsync.spring.notification.dto;

import lombok.Builder;
import lombok.Data;
import org.devsync.spring.auth.entity.User;
import org.devsync.spring.notification.entity.NotificationType;
import org.devsync.spring.notification.entity.ResourceType;

import java.util.List;
import java.util.UUID;

@Builder
@Data
public class CreateNotificationRequest {
    private String title;
    private String message;
    private NotificationType notificationType;
    private ResourceType resourceType;
    private UUID workspaceId;
    private UUID projectId;
    private UUID resourceId;
    private List<User> recipients;
}
