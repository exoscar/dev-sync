package org.devsync.spring.workspace.event;

import java.util.UUID;

public record WorkspaceMembershipCacheInvalidationEvent(
        UUID workspaceId,
        UUID userId
) {
}
