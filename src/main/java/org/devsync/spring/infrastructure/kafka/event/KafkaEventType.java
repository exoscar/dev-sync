package org.devsync.spring.infrastructure.kafka.event;

public enum KafkaEventType {
    ISSUE_ASSIGNED,
    ISSUE_STATUS_CHANGED,
    ISSUE_PRIORITY_CHANGED,
    COMMENT_CREATED,
    LABEL_ADDED,
    LABEL_REMOVED
}
