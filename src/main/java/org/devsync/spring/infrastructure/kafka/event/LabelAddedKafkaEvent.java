package org.devsync.spring.infrastructure.kafka.event;

import java.util.UUID;

public record LabelAddedKafkaEvent(
        UUID eventId,
        UUID issueId,
        UUID labelId,
        UUID actorId,
        UUID workspaceId,
        UUID projectId,
        String labelName,
        String issueTitle
) implements KafkaNotificationEvent {

    @Override
    public UUID aggregateId() {
        return issueId;
    }

    @Override
    public KafkaEventType eventType() {
        return KafkaEventType.LABEL_ADDED;
    }
}
