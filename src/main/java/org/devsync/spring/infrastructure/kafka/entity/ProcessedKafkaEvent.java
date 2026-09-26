package org.devsync.spring.infrastructure.kafka.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Table(name = "processed_kafka_events")
@IdClass(ProcessedKafkaEventId.class)
@NoArgsConstructor
public class ProcessedKafkaEvent {
    @Id
    private UUID eventId;

    @Id
    private String consumerType;

    private Instant processedAt;

    public ProcessedKafkaEvent(UUID eventId, String consumerType) {
        this.eventId = eventId;
        this.consumerType = consumerType;
        processedAt = Instant.now();
    }

}
