package org.devsync.spring.infrastructure.kafka.event;

import java.util.UUID;

public record CommentCreatedKafkaEvent(
        UUID eventId,
        UUID issueId,
        UUID commentId,
        UUID actorId,
        UUID workspaceId,
        UUID projectId,
        String firstName,
        String issueTitle
) implements KafkaNotificationEvent {


    @Override
    public UUID aggregateId() {
        return issueId;
    }

    @Override
    public KafkaEventType eventType() {
        return KafkaEventType.COMMENT_CREATED;
    }
}