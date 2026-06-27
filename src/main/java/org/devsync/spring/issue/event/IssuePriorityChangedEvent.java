package org.devsync.spring.issue.event;

import org.devsync.spring.issue.entity.IssuePriority;

import java.util.UUID;

public record IssuePriorityChangedEvent(  UUID issueId,
                                          UUID actorId,
                                          IssuePriority oldPriority,
                                          IssuePriority newPriority,
                                          UUID workspaceId,
                                          UUID projectId) {
}
