package org.devsync.spring.infrastructure.kafka.event;

import java.util.UUID;

public record IssueAssignedKafkaEvent(
        UUID eventId,
        UUID issueId,
        String title,
        String description,
        UUID actorId,
        UUID assigneeId,
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
        return KafkaEventType.ISSUE_ASSIGNED;
    }
}
