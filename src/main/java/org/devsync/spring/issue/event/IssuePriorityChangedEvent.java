package org.devsync.spring.issue.event;

import org.devsync.spring.issue.entity.IssuePriority;

import java.util.UUID;

public record IssuePriorityChangedEvent(  UUID issueId,
                                          String title,
                                          String description,
                                          UUID actorId,
                                          IssuePriority oldPriority,
                                          IssuePriority newPriority,
                                          UUID workspaceId,
                                          String workspaceName,
                                          UUID projectId,
                                          String projectName) {
}
