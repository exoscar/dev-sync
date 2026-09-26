package org.devsync.spring.infrastructure.kafka.entity;

import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.UUID;

@EqualsAndHashCode
public class ProcessedKafkaEventId implements Serializable {

    private UUID eventId;
    private String consumerType;

    public ProcessedKafkaEventId() {
    }

    public ProcessedKafkaEventId(
            UUID eventId,
            String consumerType
    ) {
        this.eventId = eventId;
        this.consumerType = consumerType;
    }
}