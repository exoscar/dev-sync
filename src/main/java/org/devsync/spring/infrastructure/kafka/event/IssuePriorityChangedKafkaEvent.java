package org.devsync.spring.infrastructure.kafka.event;

import org.devsync.spring.issue.entity.IssuePriority;

import java.util.UUID;

public record IssuePriorityChangedKafkaEvent(
        UUID eventId,
        UUID issueId,
        String title,
        String description,
        UUID actorId,
        IssuePriority oldPriority,
        IssuePriority newPriority,
        UUID workspaceId,
        String workspaceName,
        UUID projectId,
        String projectName
) implements KafkaNotificationEvent {

    @Override
    public UUID aggregateId() {
        return issueId;
    }

    @Override
    public KafkaEventType eventType() {
        return KafkaEventType.ISSUE_PRIORITY_CHANGED;
    }
}