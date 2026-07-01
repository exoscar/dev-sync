package org.devsync.spring.email.listener;


import lombok.RequiredArgsConstructor;
import org.devsync.spring.auth.entity.User;
import org.devsync.spring.auth.repository.UserRepository;
import org.devsync.spring.email.dto.EmailRecipient;
import org.devsync.spring.email.dto.IssueAssignmentRequest;
import org.devsync.spring.email.dto.IssuePriorityChange;
import org.devsync.spring.email.dto.IssueStatusChange;
import org.devsync.spring.email.mapper.EmailMapper;
import org.devsync.spring.email.service.EmailNotificationService;
import org.devsync.spring.issue.event.IssueAssignedEvent;
import org.devsync.spring.issue.event.IssuePriorityChangedEvent;
import org.devsync.spring.issue.event.IssueStatusChangedEvent;
import org.devsync.spring.notification.dto.CreateNotificationRequest;
import org.devsync.spring.notification.entity.NotificationType;
import org.devsync.spring.notification.entity.ResourceType;
import org.devsync.spring.watcher.entity.IssueWatcher;
import org.devsync.spring.watcher.service.IssueWatcherAccessService;
import org.devsync.spring.watcher.service.IssueWatcherService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EmailNotificationListener {
    private final EmailNotificationService emailNotificationService;
    private final UserRepository userRepository;
    private final IssueWatcherAccessService issueWatcherAccessService;
    private final EmailMapper mapper;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleIssueAssigned(IssueAssignedEvent issueAssignedEvent){
        if (issueAssignedEvent.actorId().equals(issueAssignedEvent.assigneeId())) {
            return;
        }
        User assignee = userRepository.getReferenceById(issueAssignedEvent.assigneeId());
        IssueAssignmentRequest request = IssueAssignmentRequest.builder()
                .title(issueAssignedEvent.title())
                .description(issueAssignedEvent.description())
                .emailRecipients(List.of(mapper.toEmailRecipient(assignee)))
                .projectName(issueAssignedEvent.projectName())
                .workspaceName(issueAssignedEvent.workspaceName())
                .build();
        emailNotificationService.sendIssueAssignedEmail(request);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleStatusChange(IssueStatusChangedEvent event){
        List<User> users = getWatcherRecipients(event.issueId(), event.actorId());
        if (users.isEmpty()) {
            return;
        }
        IssueStatusChange request = IssueStatusChange.builder()
                .title(event.title())
                .description(event.description())
                .emailRecipients(users.stream().map(mapper::toEmailRecipient).toList())
                .projectName(event.projectName())
                .workspaceName(event.workspaceName())
                .oldStatus(event.oldStatus())
                .newStatus(event.newStatus())
                .build();

        emailNotificationService.sendIssueStatusChangedEmail(request);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePriorityChange(IssuePriorityChangedEvent event){
        List<User> users = getWatcherRecipients(event.issueId(), event.actorId());
        if (users.isEmpty()) {
            return;
        }
       IssuePriorityChange request = IssuePriorityChange.builder()
                .title(event.title())
                .description(event.description())
                .emailRecipients(users.stream().map(mapper::toEmailRecipient).toList())
                .projectName(event.projectName())
                .workspaceName(event.workspaceName())
                .oldPriority(event.oldPriority())
                .newPriority(event.newPriority())
                .build();

        emailNotificationService.sendIssuePriorityChangedEmail(request);
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
