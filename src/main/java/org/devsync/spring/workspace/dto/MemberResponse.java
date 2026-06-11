package org.devsync.spring.workspace.dto;

import lombok.Builder;
import lombok.Data;
import org.devsync.spring.workspace.entity.WorkspaceRole;

import java.util.UUID;

@Data
@Builder
public class MemberResponse {
    private UUID id;
    private String email;
    private WorkspaceRole role;
    private String workspaceName;
}
