package org.devsync.spring.infrastructure.outbox.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devsync.spring.infrastructure.outbox.entity.OutboxEvent;
import org.devsync.spring.infrastructure.outbox.repository.OutboxEventRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxService {
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;


    public void save(
            String aggregateType,
            UUID aggregateId,
            String eventType,
            Object event
    ) {
        try {
            log.info(
                    "Saving outbox event: aggregateType={}, aggregateId={}, eventType={}",
                    aggregateType,
                    aggregateId,
                    eventType
            );

            String payload = objectMapper.writeValueAsString(event);

            OutboxEvent outboxEvent = new OutboxEvent(
                    UUID.randomUUID(),
                    aggregateType,
                    aggregateId, eventType, payload
            );

            outboxEventRepository.save(outboxEvent);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize outbox event");
        }
    }
}
