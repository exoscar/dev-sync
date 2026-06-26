package org.devsync.spring.notification.dto;

import lombok.Builder;

@Builder
public record UnreadCountResponse(
        long count
) {
}