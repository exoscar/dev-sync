package org.devsync.spring.workspace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class DeleteMemberRequest {
    @NotNull
    private UUID id;
}
