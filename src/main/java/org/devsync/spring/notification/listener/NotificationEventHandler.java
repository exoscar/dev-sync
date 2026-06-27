package org.devsync.spring.notification.listener;

import lombok.RequiredArgsConstructor;
import org.devsync.spring.auth.entity.User;
import org.devsync.spring.auth.repository.UserRepository;
import org.devsync.spring.comment.event.CommentCreatedEvent;
import org.devsync.spring.issue.event.IssueAssignedEvent;
import org.devsync.spring.issue.event.IssuePriorityChangedEvent;
import org.devsync.spring.issue.event.IssueStatusChangedEvent;
import org.devsync.spring.label.event.LabelAddedEvent;
import org.devsync.spring.label.event.LabelRemovedEvent;
import org.devsync.spring.notification.dto.CreateNotificationRequest;
import org.devsync.spring.notification.entity.NotificationType;
import org.devsync.spring.notification.entity.ResourceType;
import org.devsync.spring.notification.service.NotificationService;
import org.devsync.spring.watcher.entity.IssueWatcher;
import org.devsync.spring.watcher.service.IssueWatcherAccessService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class NotificationEventHandler {
    private final IssueWatcherAccessService issueWatcherAccessService;
    private final NotificationService notificationService;
    private final UserRepository userRepository;


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleIssueAssigned(IssueAssignedEvent issueAssignedEvent) {
        if (issueAssignedEvent.actorId().equals(issueAssignedEvent.assigneeId())) {
            return;
        }
        User assignee = userRepository.getReferenceById(issueAssignedEvent.assigneeId());
        CreateNotificationRequest request = CreateNotificationRequest.builder()
                .title("Issue Assigned")
                .message("You have been assigned an issue")
                .notificationType(NotificationType.ISSUE_ASSIGNED)
                .resourceType(ResourceType.ISSUE)
                .workspaceId(issueAssignedEvent.workspaceId())
                .projectId(issueAssignedEvent.projectId())
                .resourceId(issueAssignedEvent.issueId())
                .recipients(List.of(assignee))
                .build();
        notificationService.createNotification(request);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleIssueStatusChange(IssueStatusChangedEvent event) {
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

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleIssuePriorityChange(IssuePriorityChangedEvent event) {
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

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCommentCreation(CommentCreatedEvent event) {
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

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleLabelAdded(LabelAddedEvent event) {
        List<User> users = getWatcherRecipients(event.issueId(), event.actorId());
        if (users.isEmpty()) {
            return;
        }
        CreateNotificationRequest request = CreateNotificationRequest.builder()
                .title("Label Added")
                .message("Label '"+event.labelName()+"' was added to issue '"+event.issueTitle()+"'")
                .notificationType(NotificationType.LABEL_ADDED)
                .resourceType(ResourceType.ISSUE).workspaceId(event.workspaceId())
                .projectId(event.projectId())
                .resourceId(event.issueId())
                .recipients(users)
                .build();
        notificationService.createNotification(request);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleLabelRemoved(LabelRemovedEvent event) {
        List<User> users = getWatcherRecipients(event.issueId(), event.actorId());
        if (users.isEmpty()) {
            return;
        }
        CreateNotificationRequest request = CreateNotificationRequest.builder()
                .title("Label Removed")
                .message("Label '"+event.labelName()+"' was removed from issue '"+event.issueTitle()+"'")
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
