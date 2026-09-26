package org.devsync.spring.infrastructure.outbox.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devsync.spring.infrastructure.kafka.KafkaEventProducer;
import org.devsync.spring.infrastructure.kafka.event.KafkaNotificationEvent;
import org.devsync.spring.infrastructure.kafka.event.KafkaNotificationEventMapper;
import org.devsync.spring.infrastructure.outbox.entity.OutboxEvent;
import org.devsync.spring.infrastructure.outbox.repository.OutboxEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxPublisher {

    private static final String ISSUE_EVENTS_TOPIC = "devsync.issue-events";

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaEventProducer kafkaEventProducer;
    private final ObjectMapper objectMapper;
    private final KafkaNotificationEventMapper mapper;

    @Transactional
    public void publishPendingEvents() {
        List<OutboxEvent> eventList = outboxEventRepository.findUnpublishedEventsForUpdate();
        for (OutboxEvent event : eventList) {
            try {
                KafkaNotificationEvent kafkaEvent =
                        mapper.map(event);

                kafkaEventProducer.sendAndWait(
                        ISSUE_EVENTS_TOPIC,
                        event.getAggregateId().toString(),
                        kafkaEvent
                );
                event.markPublished();
                log.info(
                        "Published outbox event: id={}, eventType={}, aggregateId={}",
                        event.getId(),
                        event.getEventType(),
                        event.getAggregateId()
                );
            } catch (Exception e) {
                log.error(
                        "Failed to publish outbox event: id={}",
                        event.getId(),
                        e
                );
            }
        }
    }


}
