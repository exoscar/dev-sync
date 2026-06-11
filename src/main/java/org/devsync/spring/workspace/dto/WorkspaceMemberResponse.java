package org.devsync.spring.workspace.dto;

import lombok.Builder;
import lombok.Data;
import org.devsync.spring.workspace.entity.WorkspaceRole;

import java.util.UUID;

@Builder
@Data
public class WorkspaceMemberResponse {
    private UUID userId;
    private String email;
    private WorkspaceRole role;
}