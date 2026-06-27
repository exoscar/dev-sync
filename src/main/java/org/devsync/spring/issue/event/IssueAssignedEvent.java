package org.devsync.spring.issue.event;
import java.util.UUID;

public record IssueAssignedEvent( UUID issueId,
                                  UUID actorId,
                                  UUID assigneeId,
                                  UUID workspaceId,
                                  UUID projectId) {
}
