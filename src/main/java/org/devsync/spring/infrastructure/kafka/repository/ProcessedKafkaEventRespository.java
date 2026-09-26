package org.devsync.spring.infrastructure.kafka.repository;

import org.devsync.spring.infrastructure.kafka.entity.ProcessedKafkaEvent;
import org.devsync.spring.infrastructure.kafka.entity.ProcessedKafkaEventId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedKafkaEventRespository extends JpaRepository<ProcessedKafkaEvent, ProcessedKafkaEventId> {
}
