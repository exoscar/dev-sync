package org.devsync.spring.infrastructure.kafka.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Setter
@Getter
@Table(name = "processed_kafka_events")
@NoArgsConstructor
public class ProcessedKafkaEvent {
    @Id
    private UUID eventId;

    private Instant processedAt;

    public ProcessedKafkaEvent(UUID eventId) {
        this.eventId = eventId;
        processedAt = Instant.now();
    }

}
