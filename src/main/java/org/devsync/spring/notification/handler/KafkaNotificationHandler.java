package org.devsync.spring.notification.handler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devsync.spring.auth.entity.User;
import org.devsync.spring.auth.repository.UserRepository;
import org.devsync.spring.infrastructure.kafka.entity.ProcessedKafkaEvent;
import org.devsync.spring.infrastructure.kafka.event.*;
import org.devsync.spring.infrastructure.kafka.repository.ProcessedKafkaEventRespository;
import org.devsync.spring.notification.dto.CreateNotificationRequest;
import org.devsync.spring.notification.entity.NotificationType;
import org.devsync.spring.notification.entity.ResourceType;
import org.devsync.spring.notification.service.NotificationService;
import org.devsync.spring.watcher.entity.IssueWatcher;
import org.devsync.spring.watcher.service.IssueWatcherAccessService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaNotificationHandler {

    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final ProcessedKafkaEventRespository processedKafkaEventRepository;
    private final IssueWatcherAccessService issueWatcherAccessService;

    @Transactional
    public void handle(KafkaNotificationEvent event) {

        if (processedKafkaEventRepository.existsById(event.eventId())) {
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

            case COMMENT_CREATED -> handleCommentCreated(
                    (CommentCreatedKafkaEvent) event
            );

            case LABEL_ADDED -> handleLabelAdded(
                    (LabelAddedKafkaEvent) event
            );

            case LABEL_REMOVED -> handleLabelRemoved(
                    (LabelRemovedKafkaEvent) event
            );
        }

        processedKafkaEventRepository.save(
                new ProcessedKafkaEvent(event.eventId())
        );
    }

    private void handleIssueAssigned(
            IssueAssignedKafkaEvent event
    ) {
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

    private void handleIssueStatusChanged(
            IssueStatusChangedKafkaEvent event
    ) {
        List<User> users =
                getWatcherRecipients(
                        event.issueId(),
                        event.actorId()
                );
        if (users.isEmpty()) {
            return;
        }
        CreateNotificationRequest request = CreateNotificationRequest.builder()
                .title("Issue Status Changed")
                .message("Status Changed from " + event.oldStatus() + " to " + event.newStatus())
                .notificationType(NotificationType.ISSUE_STATUS_CHANGED)
                .resourceType(ResourceType.ISSUE).workspaceId(event.workspaceId())
                .projectId(event.projectId())
                .resourceId(event.issueId())
                .recipients(users)
                .build();

        notificationService.createNotification(request);
    }

    private void handleIssuePriorityChanged(
            IssuePriorityChangedKafkaEvent event
    ) {
        List<User> users = getWatcherRecipients(event.issueId(), event.actorId());
        if (users.isEmpty()) {
            return;
        }
        CreateNotificationRequest request = CreateNotificationRequest.builder()
                .title("Issue Priority Changed")
                .message("Priority Changed from " + event.oldPriority() + " to " + event.newPriority())
                .notificationType(NotificationType.ISSUE_PRIORITY_CHANGED)
                .resourceType(ResourceType.ISSUE).workspaceId(event.workspaceId())
                .projectId(event.projectId())
                .resourceId(event.issueId())
                .recipients(users)
                .build();
        notificationService.createNotification(request);
    }

    private void handleCommentCreated(
            CommentCreatedKafkaEvent event
    ) {
        List<User> users = getWatcherRecipients(event.issueId(), event.actorId());
        if (users.isEmpty()) {
            return;
        }
        CreateNotificationRequest request = CreateNotificationRequest.builder()
                .title(event.firstName() + " added a comment")
                .message("Commented on Issue: " + event.issueTitle())
                .notificationType(NotificationType.ISSUE_COMMENTED)
                .resourceType(ResourceType.COMMENT).workspaceId(event.workspaceId())
                .projectId(event.projectId())
                .resourceId(event.commentId())
                .recipients(users)
                .build();
        notificationService.createNotification(request);
    }

    private void handleLabelAdded(
            LabelAddedKafkaEvent event
    ) {
        List<User> users = getWatcherRecipients(event.issueId(), event.actorId());
        if (users.isEmpty()) {
            return;
        }
        CreateNotificationRequest request = CreateNotificationRequest.builder()
                .title("Label Added")
                .message("Label '" + event.labelName() + "' was added to issue '" + event.issueTitle() + "'")
                .notificationType(NotificationType.LABEL_ADDED)
                .resourceType(ResourceType.ISSUE).workspaceId(event.workspaceId())
                .projectId(event.projectId())
                .resourceId(event.issueId())
                .recipients(users)
                .build();
        notificationService.createNotification(request);
    }

    private void handleLabelRemoved(
            LabelRemovedKafkaEvent event
    ) {
        List<User> users = getWatcherRecipients(event.issueId(), event.actorId());
        if (users.isEmpty()) {
            return;
        }
        CreateNotificationRequest request = CreateNotificationRequest.builder()
                .title("Label Removed")
                .message("Label '" + event.labelName() + "' was removed from issue '" + event.issueTitle() + "'")
                .notificationType(NotificationType.LABEL_REMOVED)
                .resourceType(ResourceType.ISSUE).workspaceId(event.workspaceId())
                .projectId(event.projectId())
                .resourceId(event.issueId())
                .recipients(users)
                .build();
        notificationService.createNotification(request);
    }

    private List<User> getWatcherRecipients(
            UUID issueId,
            UUID actorId
    ) {
        return issueWatcherAccessService
                .getIssueWatchers(issueId)
                .stream()
                .map(IssueWatcher::getUser)
                .filter(user -> !user.getId().equals(actorId))
                .toList();
    }
}