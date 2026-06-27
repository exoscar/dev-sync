package org.devsync.spring.issue.event;

import org.devsync.spring.issue.entity.IssueStatus;

import java.util.UUID;

public record IssueStatusChangedEvent(UUID issueId,
                                      UUID actorId,
                                      IssueStatus oldStatus,
                                      IssueStatus newStatus,
                                      UUID workspaceId,
                                      UUID projectId) {
}
