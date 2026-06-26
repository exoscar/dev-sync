package org.devsync.spring.notification.service;

import lombok.RequiredArgsConstructor;
import org.devsync.spring.auth.entity.User;
import org.devsync.spring.common.exception.BusinessException;
import org.devsync.spring.common.exception.ErrorCode;
import org.devsync.spring.common.security.CurrentUserService;
import org.devsync.spring.notification.entity.Notification;
import org.devsync.spring.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class NotificationAccessService {

    private final NotificationRepository repository;

    public Notification getAccessibleNotification(
            UUID notificationId,
            UUID currUserId
    ) {
        Notification notification = repository.findById(notificationId).orElseThrow(
                () -> new BusinessException("Notification not found", ErrorCode.NOTIFICATION_NOT_FOUND)
        );
        if (!notification.getRecipient().getId().equals(currUserId)) {
            throw new BusinessException("Access denied: You do not have to this notification", ErrorCode.FORBIDDEN);
        }
        return notification;
    }
}
