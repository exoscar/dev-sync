package org.devsync.spring.workspace.dto;

import org.devsync.spring.workspace.entity.WorkspaceRole;

import java.util.UUID;

public record WorkspaceMembershipCacheEntry(
        UUID userId,
        UUID workspaceId,
        WorkspaceRole role
) {
}