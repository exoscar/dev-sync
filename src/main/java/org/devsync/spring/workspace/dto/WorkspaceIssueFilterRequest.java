package org.devsync.spring.workspace.dto;

import lombok.Builder;
import lombok.Data;
import org.devsync.spring.issue.entity.IssuePriority;
import org.devsync.spring.issue.entity.IssueStatus;

import java.time.Instant;
import java.util.UUID;

@Builder
@Data
public class WorkspaceIssueFilterRequest {
    private IssueStatus status;
    private IssuePriority priority;
    private UUID assigneeId;
    private UUID projectId;
    private Boolean unassigned;
    private Instant createdFrom;
    private Instant createdTo;
    private Instant updatedFrom;
    private Instant updatedTo;
}
