package org.devsync.spring.issue.event;

import org.devsync.spring.issue.entity.IssueStatus;

import java.util.UUID;

public record IssueStatusChangedEvent(UUID issueId,
                                      String title,
                                      String description,
                                      UUID actorId,
                                      IssueStatus oldStatus,
                                      IssueStatus newStatus,
                                      UUID workspaceId,
                                      String workspaceName,
                                      UUID projectId,
                                      String projectName) {
}
