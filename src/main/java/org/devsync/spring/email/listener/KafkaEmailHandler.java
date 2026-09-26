package org.devsync.spring.email.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devsync.spring.auth.entity.User;
import org.devsync.spring.auth.repository.UserRepository;
import org.devsync.spring.common.exception.BusinessException;
import org.devsync.spring.common.exception.ErrorCode;
import org.devsync.spring.email.dto.EmailRecipient;
import org.devsync.spring.email.dto.IssueAssignmentRequest;
import org.devsync.spring.email.dto.IssuePriorityChange;
import org.devsync.spring.email.dto.IssueStatusChange;
import org.devsync.spring.email.mapper.EmailMapper;
import org.devsync.spring.email.service.EmailNotificationService;
import org.devsync.spring.infrastructure.kafka.entity.KafkaConsumerType;
import org.devsync.spring.infrastructure.kafka.entity.ProcessedKafkaEvent;
import org.devsync.spring.infrastructure.kafka.entity.ProcessedKafkaEventId;
import org.devsync.spring.infrastructure.kafka.event.IssueAssignedKafkaEvent;
import org.devsync.spring.infrastructure.kafka.event.IssuePriorityChangedKafkaEvent;
import org.devsync.spring.infrastructure.kafka.event.IssueStatusChangedKafkaEvent;
import org.devsync.spring.infrastructure.kafka.event.KafkaNotificationEvent;
import org.devsync.spring.infrastructure.kafka.repository.ProcessedKafkaEventRespository;
import org.devsync.spring.watcher.service.IssueWatcherAccessService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaEmailHandler {
    private final EmailNotificationService emailNotificationService;
    private final UserRepository userRepository;
    private final IssueWatcherAccessService issueWatcherAccessService;
    private final EmailMapper mapper;
    private final ProcessedKafkaEventRespository processedKafkaEventRespository;

    @Transactional
    public void handle(KafkaNotificationEvent event) {
        ProcessedKafkaEventId id = new ProcessedKafkaEventId(
                event.eventId(),
                KafkaConsumerType.EMAIL.name()
        );

        if (processedKafkaEventRespository.existsById(id)) {
            log.info(
                    "Kafka event already processed: eventId={}",
                    event.eventId()
            );
            return;
        }

        switch (event.eventType()) {

            case ISSUE_ASSIGNED -> handleIssueAssigned(
                    (IssueAssignedKafkaEvent) event
            );

            case ISSUE_STATUS_CHANGED -> handleIssueStatusChanged(
                    (IssueStatusChangedKafkaEvent) event
            );

            case ISSUE_PRIORITY_CHANGED -> handleIssuePriorityChanged(
                    (IssuePriorityChangedKafkaEvent) event
            );
        }

        processedKafkaEventRespository.save(
                new ProcessedKafkaEvent(
                        event.eventId(),
                        KafkaConsumerType.EMAIL.name()
                )
        );
    }

    private void handleIssueAssigned(
            IssueAssignedKafkaEvent event
    ) {
        if (event.actorId().equals(event.assigneeId())) {
            return;
        }
        User assignee = userRepository.findById(event.assigneeId())
                .orElseThrow(() -> new BusinessException("Assignee Not Found", ErrorCode.NOT_FOUND));
        IssueAssignmentRequest request = IssueAssignmentRequest.builder()
                .title(event.title())
                .description(event.description())
                .emailRecipients(List.of(mapper.toEmailRecipient(assignee)))
                .projectName(event.projectName())
                .workspaceName(event.workspaceName())
                .build();
        emailNotificationService.sendIssueAssignedEmail(request);
    }

    private void handleIssueStatusChanged(
            IssueStatusChangedKafkaEvent event
    ) {
        List<EmailRecipient> users =
                issueWatcherAccessService.getWatcherEmailRecipients(
                        event.issueId(),
                        event.actorId()
                );
        if (users.isEmpty()) {
            return;
        }
        IssueStatusChange request = IssueStatusChange.builder()
                .title(event.title())
                .description(event.description())
                .emailRecipients(users)
                .projectName(event.projectName())
                .workspaceName(event.workspaceName())
                .oldStatus(event.oldStatus())
                .newStatus(event.newStatus())
                .build();

        emailNotificationService.sendIssueStatusChangedEmail(request);
    }

    private void handleIssuePriorityChanged(
            IssuePriorityChangedKafkaEvent event
    ) {
        List<EmailRecipient> users =
                issueWatcherAccessService.getWatcherEmailRecipients(
                        event.issueId(),
                        event.actorId()
                );
        if (users.isEmpty()) {
            return;
        }
        IssuePriorityChange request = IssuePriorityChange.builder()
                .title(event.title())
                .description(event.description())
                .emailRecipients(users)
                .projectName(event.projectName())
                .workspaceName(event.workspaceName())
                .oldPriority(event.oldPriority())
                .newPriority(event.newPriority())
                .build();

        emailNotificationService.sendIssuePriorityChangedEmail(request);
    }


}
