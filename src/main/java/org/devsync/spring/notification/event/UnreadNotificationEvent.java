package org.devsync.spring.notification.event;

import java.util.UUID;

public record UnreadNotificationEvent(
        UUID userId
) {
}
