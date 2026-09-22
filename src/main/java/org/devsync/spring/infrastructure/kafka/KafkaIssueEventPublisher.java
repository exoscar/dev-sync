package org.devsync.spring.infrastructure.kafka;


import lombok.RequiredArgsConstructor;
import org.devsync.spring.infrastructure.kafka.event.IssueAssignedKafkaEvent;
import org.devsync.spring.issue.event.IssueAssignedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class KafkaIssueEventPublisher {
    private static final String ISSUE_EVENTS_TOPIC = "devsync.issue-events";

    private final KafkaEventProducer kafkaEventProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishIssueAssigned(IssueAssignedEvent event){
        IssueAssignedKafkaEvent kafkaEvent =
                new IssueAssignedKafkaEvent(
                        UUID.randomUUID(),
                        event.issueId(),
                        event.title(),
                        event.description(),
                        event.actorId(),
                        event.assigneeId(),
                        event.workspaceId(),
                        event.workspaceName(),
                        event.projectId(),
                        event.projectName()
                );

        kafkaEventProducer.send(
                ISSUE_EVENTS_TOPIC,
                event.issueId().toString(),
                kafkaEvent
        );
    }
}
