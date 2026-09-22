package org.devsync.spring.infrastructure.kafka.repository;

import org.devsync.spring.infrastructure.kafka.entity.ProcessedKafkaEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProcessedKafkaEventRespository extends JpaRepository<ProcessedKafkaEvent, UUID> {
}
