package org.devsync.spring.dashboard.event;

import java.util.UUID;

public record DashboardCacheInvalidationEvent(
        UUID workspaceId,
        UUID projectId
) {
}