package org.devsync.spring.infrastructure.outbox.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devsync.spring.infrastructure.kafka.KafkaEventProducer;
import org.devsync.spring.infrastructure.kafka.event.IssueAssignedKafkaEvent;
import org.devsync.spring.infrastructure.outbox.entity.OutboxEvent;
import org.devsync.spring.infrastructure.outbox.repository.OutboxEventRepository;
import org.devsync.spring.issue.event.IssueAssignedEvent;
import org.springframework.scheduling.annotation.Scheduled;
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

    @Transactional
    public void publishPendingEvents(){
        List<OutboxEvent> eventList = outboxEventRepository.findUnpublishedEventsForUpdate();
        for(OutboxEvent event:eventList){
            try {

                IssueAssignedEvent domainEvent =
                        objectMapper.readValue(
                                event.getPayload(),
                                IssueAssignedEvent.class
                        );

                IssueAssignedKafkaEvent kafkaEvent =
                        new IssueAssignedKafkaEvent(
                                event.getId(),                 // stable eventId
                                domainEvent.issueId(),
                                domainEvent.title(),
                                domainEvent.description(),
                                domainEvent.actorId(),
                                domainEvent.assigneeId(),
                                domainEvent.workspaceId(),
                                domainEvent.workspaceName(),
                                domainEvent.projectId(),
                                domainEvent.projectName()
                        );
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
