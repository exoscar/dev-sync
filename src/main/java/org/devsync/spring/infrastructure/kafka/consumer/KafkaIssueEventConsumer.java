package org.devsync.spring.infrastructure.kafka.consumer;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.devsync.spring.infrastructure.kafka.event.IssueAssignedKafkaEvent;
import org.devsync.spring.notification.service.IssueAssignmentNotificationService;
import org.devsync.spring.notification.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaIssueEventConsumer {

    private final IssueAssignmentNotificationService notificationService;

    //Non Blocking Retry mechanism
    @RetryableTopic(
            attempts = "4",
            backoff = @Backoff(
                    delay = 1000,
                    multiplier = 2.0
            )
    )
    @KafkaListener(
            groupId = "devsync-notification-service",
            topics = "devsync.issue-events",
            concurrency = "3"
    )
    public void consume(IssueAssignedKafkaEvent event) {
        notificationService.createNotification(event);
    }
}
