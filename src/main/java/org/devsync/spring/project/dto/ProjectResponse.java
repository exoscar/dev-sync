package org.devsync.spring.project.dto;

import lombok.Builder;
import lombok.Data;
import org.devsync.spring.workspace.entity.Workspace;

import java.util.UUID;

@Data
@Builder
public class ProjectResponse {

    private UUID id;
    private String name;
    private String description;

    private UUID workspaceId;
    private String workspaceName;

}
