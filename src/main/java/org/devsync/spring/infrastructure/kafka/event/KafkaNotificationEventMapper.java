package org.devsync.spring.infrastructure.kafka.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.devsync.spring.comment.event.CommentCreatedEvent;
import org.devsync.spring.infrastructure.outbox.entity.OutboxEvent;
import org.devsync.spring.issue.event.IssueAssignedEvent;
import org.devsync.spring.issue.event.IssuePriorityChangedEvent;
import org.devsync.spring.issue.event.IssueStatusChangedEvent;
import org.devsync.spring.label.event.LabelAddedEvent;
import org.devsync.spring.label.event.LabelRemovedEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaNotificationEventMapper {

    private final ObjectMapper objectMapper;

    public KafkaNotificationEvent map(OutboxEvent event) {

        KafkaEventType eventType =
                KafkaEventType.valueOf(event.getEventType());

        return switch (eventType) {

            case ISSUE_ASSIGNED -> mapIssueAssigned(event);

            case ISSUE_STATUS_CHANGED -> mapIssueStatusChanged(event);

            case ISSUE_PRIORITY_CHANGED -> mapIssuePriorityChanged(event);

            case COMMENT_CREATED -> mapCommentCreated(event);

            case LABEL_ADDED -> mapLabelAdded(event);

            case LABEL_REMOVED -> mapLabelRemoved(event);
        };
    }

    private IssueAssignedKafkaEvent mapIssueAssigned(
            OutboxEvent event
    ) {
        IssueAssignedEvent domainEvent =
                deserialize(event, IssueAssignedEvent.class);

        return new IssueAssignedKafkaEvent(
                event.getId(),
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
    }

    private IssueStatusChangedKafkaEvent mapIssueStatusChanged(
            OutboxEvent event
    ) {
        IssueStatusChangedEvent domainEvent =
                deserialize(event, IssueStatusChangedEvent.class);

        return new IssueStatusChangedKafkaEvent(
                event.getId(),
                domainEvent.issueId(),
                domainEvent.title(),
                domainEvent.description(),
                domainEvent.actorId(),
                domainEvent.oldStatus(),
                domainEvent.newStatus(),
                domainEvent.workspaceId(),
                domainEvent.workspaceName(),
                domainEvent.projectId(),
                domainEvent.projectName()
        );
    }

    private IssuePriorityChangedKafkaEvent mapIssuePriorityChanged(
            OutboxEvent event
    ) {
        IssuePriorityChangedEvent domainEvent =
                deserialize(event, IssuePriorityChangedEvent.class);

        return new IssuePriorityChangedKafkaEvent(
                event.getId(),
                domainEvent.issueId(),
                domainEvent.title(),
                domainEvent.description(),
                domainEvent.actorId(),
                domainEvent.oldPriority(),
                domainEvent.newPriority(),
                domainEvent.workspaceId(),
                domainEvent.workspaceName(),
                domainEvent.projectId(),
                domainEvent.projectName()
        );
    }

    private CommentCreatedKafkaEvent mapCommentCreated(
            OutboxEvent event
    ) {
        CommentCreatedEvent domainEvent =
                deserialize(event, CommentCreatedEvent.class);

        return new CommentCreatedKafkaEvent(
                event.getId(),
                domainEvent.issueId(),
                domainEvent.commentId(),
                domainEvent.actorId(),
                domainEvent.workspaceId(),
                domainEvent.projectId(),
                domainEvent.firstName(),
                domainEvent.issueTitle()
        );
    }

    private LabelAddedKafkaEvent mapLabelAdded(
            OutboxEvent event
    ) {
        LabelAddedEvent domainEvent =
                deserialize(event, LabelAddedEvent.class);

        return new LabelAddedKafkaEvent(
                event.getId(),
                domainEvent.issueId(),
                domainEvent.labelId(),
                domainEvent.actorId(),
                domainEvent.workspaceId(),
                domainEvent.projectId(),
                domainEvent.labelName(),
                domainEvent.issueTitle()
        );
    }

    private LabelRemovedKafkaEvent mapLabelRemoved(
            OutboxEvent event
    ) {
        LabelRemovedEvent domainEvent =
                deserialize(event, LabelRemovedEvent.class);

        return new LabelRemovedKafkaEvent(
                event.getId(),
                domainEvent.issueId(),
                domainEvent.labelId(),
                domainEvent.actorId(),
                domainEvent.workspaceId(),
                domainEvent.projectId(),
                domainEvent.labelName(),
                domainEvent.issueTitle()
        );
    }

    private <T> T deserialize(
            OutboxEvent event,
            Class<T> type
    ) {
        try {
            return objectMapper.readValue(
                    event.getPayload(),
                    type
            );
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(
                    "Failed to deserialize outbox event: "
                            + event.getEventType()
                            + ", id=" + event.getId(),
                    e
            );
        }
    }
}
