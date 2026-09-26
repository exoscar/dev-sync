package org.devsync.spring.infrastructure.kafka.event;

import java.util.UUID;

public interface KafkaNotificationEvent {

    UUID eventId();

    UUID aggregateId();

    KafkaEventType eventType();
}
