package org.devsync.spring.workspace.event;

import java.util.UUID;

public record WorkspaceMembershipChangedEvent(
        UUID workspaceId,
        UUID userId
) {
}
