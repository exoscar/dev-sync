package org.devsync.spring.notification.mapper;

import org.devsync.spring.notification.dto.NotificationResponse;
import org.devsync.spring.notification.dto.UnreadCountResponse;
import org.devsync.spring.notification.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationResponse toResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .type(notification.getType())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .resourceType(notification.getResourceType())
                .resourceId(notification.getResourceId())
                .read(notification.isRead())
                .readAt(notification.getReadAt())
                .createdAt(notification.getCreatedAt())
                .projectId(notification.getProjectId())
                .workspaceId(notification.getWorkspaceId())
                .build();
    }

    public UnreadCountResponse toRespone(long count){
        return UnreadCountResponse.builder().count(count).build();
    }
}
