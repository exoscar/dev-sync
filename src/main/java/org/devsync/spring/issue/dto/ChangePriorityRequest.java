package org.devsync.spring.issue.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devsync.spring.issue.entity.IssuePriority;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ChangePriorityRequest {
    @NotNull
    private IssuePriority issuePriority;
}
