package org.devsync.spring.workspace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devsync.spring.workspace.entity.WorkspaceRole;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRoleRequest {
    @NotNull
    private UUID id;
    @NotNull
    private WorkspaceRole role;
}
