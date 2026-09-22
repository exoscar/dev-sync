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
) {
}
