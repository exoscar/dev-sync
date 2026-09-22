package org.devsync.spring.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devsync.spring.auth.entity.User;
import org.devsync.spring.auth.repository.UserRepository;
import org.devsync.spring.infrastructure.kafka.entity.ProcessedKafkaEvent;
import org.devsync.spring.infrastructure.kafka.event.IssueAssignedKafkaEvent;
import org.devsync.spring.infrastructure.kafka.repository.ProcessedKafkaEventRespository;
import org.devsync.spring.notification.dto.CreateNotificationRequest;
import org.devsync.spring.notification.entity.NotificationType;
import org.devsync.spring.notification.entity.ResourceType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class IssueAssignmentNotificationService {
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final ProcessedKafkaEventRespository processedKafkaEventRepository;

    @Transactional
    public void createNotification(IssueAssignedKafkaEvent event) {
        log.info(
                "Processing IssueAssignedKafkaEvent: eventId={}, issueId={}",
                event.eventId(),
                event.issueId()
        );
        if (processedKafkaEventRepository.existsById(event.eventId())) {
            log.info("Kafka event already processed: eventId={}", event.eventId());
            return;
        }

        if (event.actorId().equals(event.assigneeId())) {
            return;
        }

        User assignee =
                userRepository.getReferenceById(event.assigneeId());

        CreateNotificationRequest request =
                CreateNotificationRequest.builder()
                        .title("Issue Assigned")
                        .message("You have been assigned an issue")
                        .notificationType(NotificationType.ISSUE_ASSIGNED)
                        .resourceType(ResourceType.ISSUE)
                        .workspaceId(event.workspaceId())
                        .projectId(event.projectId())
                        .resourceId(event.issueId())
                        .recipients(List.of(assignee))
                        .build();

        notificationService.createNotification(request);
        processedKafkaEventRepository.save(
                new ProcessedKafkaEvent(event.eventId())
        );
    }
}
