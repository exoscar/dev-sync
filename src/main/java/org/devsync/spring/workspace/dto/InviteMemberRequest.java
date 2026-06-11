package org.devsync.spring.workspace.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devsync.spring.workspace.entity.WorkspaceRole;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InviteMemberRequest {
    @NotBlank(message = "Email cannot be null")
    @Email(message = "Invalid Email Format")
    private String email;

    @NotNull(message = "Role cannot be Null")
    private WorkspaceRole role;
}
