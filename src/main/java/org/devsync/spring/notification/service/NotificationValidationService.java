package org.devsync.spring.notification.service;

import org.devsync.spring.common.util.Utils;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NotificationValidationService {
    public UUID parseNotificationId(String id) {
        return Utils.parseUuid(id, "Invalid Notification Id");
    }
}
