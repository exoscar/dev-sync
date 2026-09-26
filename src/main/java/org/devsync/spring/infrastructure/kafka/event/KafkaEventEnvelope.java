package org.devsync.spring.infrastructure.kafka.event;

import java.util.UUID;

public record KafkaEventEnvelope(
        UUID eventId,
        KafkaEventType eventType,
        UUID aggregateId,
        UUID workspaceId,
        Object payload
) {
}