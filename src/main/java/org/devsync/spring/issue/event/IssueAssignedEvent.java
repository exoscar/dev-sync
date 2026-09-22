package org.devsync.spring.issue.event;
import java.util.UUID;

public record IssueAssignedEvent(
        UUID eventId,
        UUID issueId,
                                  String title,
                                  String description,
                                  UUID actorId,
                                  UUID assigneeId,
                                  UUID workspaceId,
                                  String workspaceName,
                                  UUID projectId,
                                  String projectName) {
}
