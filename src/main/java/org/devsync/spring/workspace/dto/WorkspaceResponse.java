package org.devsync.spring.workspace.dto;

import lombok.Builder;
import lombok.Data;
import org.devsync.spring.user.dto.UserResponse;

import java.util.UUID;

@Data
@Builder
public class WorkspaceResponse {
    private UUID id;
    private String name;
    private UserResponse owner;
}
